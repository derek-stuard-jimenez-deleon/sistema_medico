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

import java.time.LocalDateTime;

@Service
public class SignosVitalesService {

    private final SignosVitalesRepository signosVitalesRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    public SignosVitalesService(SignosVitalesRepository signosVitalesRepository,
                                CitaRepository citaRepository,
                                UsuarioRepository usuarioRepository) {
        this.signosVitalesRepository = signosVitalesRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
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

        SignosVitales guardado = signosVitalesRepository.save(signos);
        return mapearAResponse(guardado);
    }

    public SignosVitalesResponse buscarPorCita(Long citaId) {
        SignosVitales signos = signosVitalesRepository.findByCitaId(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay signos vitales registrados para esta cita."));
        return mapearAResponse(signos);
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
        return dto;
    }
}