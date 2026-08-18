package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.SignosVitalesRequest;
import com.sistemamedico.app.dto.SignosVitalesResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Cita;
import com.sistemamedico.app.model.SignosVitales;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.CitaRepository;
import com.sistemamedico.app.repository.SignosVitalesRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SignosVitalesService {

    // Rangos clínicos normales (RN-CU07-06) - distintos del rango de captura (RN-CU07-01 a 05)
    private static final int PRESION_SISTOLICA_NORMAL_MIN = 90;
    private static final int PRESION_SISTOLICA_NORMAL_MAX = 120;
    private static final int PRESION_DIASTOLICA_NORMAL_MIN = 60;
    private static final int PRESION_DIASTOLICA_NORMAL_MAX = 80;
    private static final BigDecimal TEMPERATURA_NORMAL_MIN = new BigDecimal("36.1");
    private static final BigDecimal TEMPERATURA_NORMAL_MAX = new BigDecimal("37.2");
    private static final int FRECUENCIA_CARDIACA_NORMAL_MIN = 60;
    private static final int FRECUENCIA_CARDIACA_NORMAL_MAX = 100;

    private final SignosVitalesRepository signosVitalesRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CitaService citaService; // Inyectar CitaService

    public SignosVitalesService(SignosVitalesRepository signosVitalesRepository,
                                CitaRepository citaRepository,
                                UsuarioRepository usuarioRepository,
                                CitaService citaService) { // Añadir CitaService al constructor
        this.signosVitalesRepository = signosVitalesRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
        this.citaService = citaService; // Asignar CitaService
    }

    @Transactional
    public SignosVitalesResponse crear(SignosVitalesRequest request) {
        Cita cita = citaRepository.findById(request.getCitaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));

        if (cita.getEstado() != Cita.EstadoCita.PAGADA) {
            throw new IllegalArgumentException("Solo se pueden tomar signos vitales de citas pagadas.");
        }

        if (signosVitalesRepository.findByCitaId(cita.getId()).isPresent()) {
            throw new IllegalArgumentException("Esta cita ya tiene signos vitales registrados.");
        }

        Usuario enfermero = usuarioRepository.findById(request.getEnfermeroId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Enfermero no encontrado."));

        SignosVitales signos = new SignosVitales();
        signos.setCita(cita);
        signos.setEnfermero(enfermero);
        signos.setPresionSistolica(request.getPresionSistolica());
        signos.setPresionDiastolica(request.getPresionDiastolica());
        signos.setTemperatura(request.getTemperatura());
        signos.setPeso(request.getPeso());
        signos.setTalla(request.getTalla());
        signos.setFrecuenciaCardiaca(request.getFrecuenciaCardiaca());
        signos.setFechaHora(LocalDateTime.now());
        signos.setEsEmergencia(request.isEsEmergencia());
        signos.setAlertasClinicas(calcularAlertasClinicas(request));

        SignosVitales guardado = signosVitalesRepository.save(signos);

        // RN-CU07-06 Post-condición: Si es emergencia, cambiar estado de la cita
        if (request.isEsEmergencia()) {
            citaService.cambiarEstado(cita.getId(), Cita.EstadoCita.PENDIENTE_CONSULTA_EMERGENCIA);
        }

        return mapearAResponse(guardado);
    }

    public SignosVitalesResponse buscarPorId(Long id) { // <-- NUEVO MÉTODO
        SignosVitales signos = signosVitalesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Signos vitales no encontrados."));
        return mapearAResponse(signos);
    }

    public SignosVitalesResponse buscarPorCita(Long citaId) {
        SignosVitales signos = signosVitalesRepository.findByCitaId(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay signos vitales registrados para esta cita."));
        return mapearAResponse(signos);
    }

    public List<SignosVitalesResponse> listarTodos() { // <-- NUEVO MÉTODO
        return signosVitalesRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    /**
     * RN-CU07-06: detecta valores dentro del rango de captura (válidos) pero
     * fuera del rango clínico normal, y arma el texto de alerta que queda
     * registrado en el expediente (postcondición del CU-07).
     */
    private String calcularAlertasClinicas(SignosVitalesRequest r) {
        List<String> alertas = new ArrayList<>();

        if (r.getPresionSistolica() != null &&
                (r.getPresionSistolica() < PRESION_SISTOLICA_NORMAL_MIN || r.getPresionSistolica() > PRESION_SISTOLICA_NORMAL_MAX)) {
            alertas.add("Presión sistólica fuera de rango normal (" + r.getPresionSistolica() + " mmHg; normal 90-120)");
        }
        if (r.getPresionDiastolica() != null &&
                (r.getPresionDiastolica() < PRESION_DIASTOLICA_NORMAL_MIN || r.getPresionDiastolica() > PRESION_DIASTOLICA_NORMAL_MAX)) {
            alertas.add("Presión diastólica fuera de rango normal (" + r.getPresionDiastolica() + " mmHg; normal 60-80)");
        }
        if (r.getTemperatura() != null &&
                (r.getTemperatura().compareTo(TEMPERATURA_NORMAL_MIN) < 0 || r.getTemperatura().compareTo(TEMPERATURA_NORMAL_MAX) > 0)) {
            alertas.add("Temperatura fuera de rango normal (" + r.getTemperatura() + " °C; normal 36.1-37.2)");
        }
        if (r.getFrecuenciaCardiaca() != null &&
                (r.getFrecuenciaCardiaca() < FRECUENCIA_CARDIACA_NORMAL_MIN || r.getFrecuenciaCardiaca() > FRECUENCIA_CARDIACA_NORMAL_MAX)) {
            alertas.add("Frecuencia cardíaca fuera de rango normal (" + r.getFrecuenciaCardiaca() + " lpm; normal 60-100)");
        }

        return alertas.isEmpty() ? null : String.join(" | ", alertas);
    }

    private SignosVitalesResponse mapearAResponse(SignosVitales signos) {
        SignosVitalesResponse dto = new SignosVitalesResponse();
        dto.setId(signos.getId());
        dto.setCitaId(signos.getCita().getId());
        dto.setPacienteNombre(signos.getCita().getPaciente().getNombreCompleto());
        dto.setEnfermeroNombre(signos.getEnfermero().getNombreCompleto());
        dto.setPresionSistolica(signos.getPresionSistolica());
        dto.setPresionDiastolica(signos.getPresionDiastolica());
        dto.setTemperatura(signos.getTemperatura());
        dto.setPeso(signos.getPeso());
        dto.setTalla(signos.getTalla());
        dto.setFrecuenciaCardiaca(signos.getFrecuenciaCardiaca());
        dto.setFechaHora(signos.getFechaHora());
        dto.setEsEmergencia(signos.getEsEmergencia());
        dto.setAlertasClinicas(signos.getAlertasClinicas());
        return dto;
    }
}