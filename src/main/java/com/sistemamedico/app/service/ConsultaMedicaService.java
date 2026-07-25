package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.ConsultaMedicaRequest;
import com.sistemamedico.app.dto.ConsultaMedicaResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Cita;
import com.sistemamedico.app.model.ConsultaMedica;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.CitaRepository;
import com.sistemamedico.app.repository.ConsultaMedicaRepository;
import com.sistemamedico.app.repository.SignosVitalesRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaMedicaService {

    private final ConsultaMedicaRepository consultaMedicaRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SignosVitalesRepository signosVitalesRepository;

    public ConsultaMedicaService(ConsultaMedicaRepository consultaMedicaRepository,
                                 CitaRepository citaRepository,
                                 UsuarioRepository usuarioRepository,
                                 SignosVitalesRepository signosVitalesRepository) {
        this.consultaMedicaRepository = consultaMedicaRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
        this.signosVitalesRepository = signosVitalesRepository;
    }

    @Transactional
    public ConsultaMedicaResponse crear(ConsultaMedicaRequest request) {
        Cita cita = citaRepository.findById(request.getCitaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));

        if (signosVitalesRepository.findByCitaId(cita.getId()).isEmpty()) {
            throw new IllegalArgumentException("Debe registrar los signos vitales antes de iniciar la consulta.");
        }

        if (consultaMedicaRepository.findByCitaId(cita.getId()).isPresent()) {
            throw new IllegalArgumentException("Esta cita ya tiene una consulta médica registrada.");
        }

        Usuario medico = usuarioRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado."));

        if (request.isCerrar() && (request.getDiagnostico() == null || request.getDiagnostico().isBlank())) {
            throw new IllegalArgumentException("El diagnóstico es obligatorio para cerrar la consulta.");
        }

        ConsultaMedica consulta = new ConsultaMedica();
        consulta.setCita(cita);
        consulta.setMedico(medico);
        consulta.setMotivoConsulta(request.getMotivoConsulta());
        consulta.setHallazgosClinicos(request.getHallazgosClinicos());
        consulta.setDiagnostico(request.getDiagnostico());
        consulta.setCodigoCie10(request.getCodigoCie10());
        consulta.setPlanTratamiento(request.getPlanTratamiento());
        consulta.setEstado(request.isCerrar()
                ? ConsultaMedica.EstadoConsulta.CERRADA
                : ConsultaMedica.EstadoConsulta.ABIERTA);

        ConsultaMedica guardada = consultaMedicaRepository.save(consulta);

        if (request.isCerrar()) {
            cita.setEstado(Cita.EstadoCita.ATENDIDA);
            citaRepository.save(cita);
        }

        return mapearAResponse(guardada);
    }

    public ConsultaMedicaResponse buscarPorCita(Long citaId) {
        ConsultaMedica consulta = consultaMedicaRepository.findByCitaId(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay consulta médica registrada para esta cita."));
        return mapearAResponse(consulta);
    }

    private ConsultaMedicaResponse mapearAResponse(ConsultaMedica consulta) {
        ConsultaMedicaResponse dto = new ConsultaMedicaResponse();
        dto.setId(consulta.getId());
        dto.setCitaId(consulta.getCita().getId());
        dto.setPacienteNombre(consulta.getCita().getPaciente().getNombreCompleto());
        dto.setMedicoNombre(consulta.getMedico().getNombreCompleto());
        dto.setMotivoConsulta(consulta.getMotivoConsulta());
        dto.setHallazgosClinicos(consulta.getHallazgosClinicos());
        dto.setDiagnostico(consulta.getDiagnostico());
        dto.setCodigoCie10(consulta.getCodigoCie10());
        dto.setPlanTratamiento(consulta.getPlanTratamiento());
        dto.setEstado(consulta.getEstado().name());
        return dto;
    }
}