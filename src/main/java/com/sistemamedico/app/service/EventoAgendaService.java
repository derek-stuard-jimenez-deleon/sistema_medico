package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.EventoAgendaRequest;
import com.sistemamedico.app.dto.EventoAgendaResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.EventoAgenda;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.EventoAgendaRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoAgendaService {

    private final EventoAgendaRepository eventoAgendaRepository;
    private final UsuarioRepository usuarioRepository;

    public EventoAgendaService(EventoAgendaRepository eventoAgendaRepository, UsuarioRepository usuarioRepository) {
        this.eventoAgendaRepository = eventoAgendaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public EventoAgendaResponse crear(EventoAgendaRequest request) {
        Usuario medico = usuarioRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado."));

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        EventoAgenda evento = new EventoAgenda();
        evento.setMedico(medico);
        evento.setTitulo(request.getTitulo());
        evento.setFechaInicio(request.getFechaInicio());
        evento.setFechaFin(request.getFechaFin());
        evento.setTipoEvento(EventoAgenda.TipoEvento.valueOf(request.getTipoEvento()));
        evento.setDescripcion(request.getDescripcion());
        evento.setColor(request.getColor());

        return mapearAResponse(eventoAgendaRepository.save(evento));
    }

    public List<EventoAgendaResponse> listarPorMedicoYRango(Long medicoId, LocalDateTime desde, LocalDateTime hasta) {
        return eventoAgendaRepository.findByMedicoIdAndFechaInicioBetween(medicoId, desde, hasta)
                .stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        EventoAgenda evento = eventoAgendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evento de agenda no encontrado."));
        evento.marcarEliminado(usuarioQueElimina);
        eventoAgendaRepository.save(evento);
    }

    private EventoAgendaResponse mapearAResponse(EventoAgenda evento) {
        EventoAgendaResponse dto = new EventoAgendaResponse();
        dto.setId(evento.getId());
        dto.setMedicoNombre(evento.getMedico().getNombreCompleto());
        dto.setTitulo(evento.getTitulo());
        dto.setFechaInicio(evento.getFechaInicio());
        dto.setFechaFin(evento.getFechaFin());
        dto.setTipoEvento(evento.getTipoEvento().name());
        dto.setDescripcion(evento.getDescripcion());
        dto.setColor(evento.getColor());
        return dto;
    }
}