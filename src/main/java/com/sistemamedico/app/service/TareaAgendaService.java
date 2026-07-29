package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.TareaAgendaRequest;
import com.sistemamedico.app.dto.TareaAgendaResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.TareaAgenda;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.TareaAgendaRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaAgendaService {

    private final TareaAgendaRepository tareaAgendaRepository;
    private final UsuarioRepository usuarioRepository;

    public TareaAgendaService(TareaAgendaRepository tareaAgendaRepository, UsuarioRepository usuarioRepository) {
        this.tareaAgendaRepository = tareaAgendaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public TareaAgendaResponse crear(TareaAgendaRequest request) {
        Usuario medico = usuarioRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado."));

        TareaAgenda tarea = new TareaAgenda();
        tarea.setMedico(medico);
        tarea.setTitulo(request.getTitulo());
        tarea.setPrioridad(TareaAgenda.Prioridad.valueOf(request.getPrioridad()));
        tarea.setFechaLimite(request.getFechaLimite());
        tarea.setEstado(TareaAgenda.EstadoTarea.PENDIENTE);

        return mapearAResponse(tareaAgendaRepository.save(tarea));
    }

    public List<TareaAgendaResponse> listarPorMedicoYEstado(Long medicoId, TareaAgenda.EstadoTarea estado) {
        return tareaAgendaRepository.findByMedicoIdAndEstado(medicoId, estado)
                .stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public TareaAgendaResponse cambiarEstado(Long id, TareaAgenda.EstadoTarea nuevoEstado) {
        TareaAgenda tarea = tareaAgendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada."));
        tarea.setEstado(nuevoEstado);
        return mapearAResponse(tareaAgendaRepository.save(tarea));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        TareaAgenda tarea = tareaAgendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada."));
        tarea.marcarEliminado(usuarioQueElimina);
        tareaAgendaRepository.save(tarea);
    }

    private TareaAgendaResponse mapearAResponse(TareaAgenda tarea) {
        TareaAgendaResponse dto = new TareaAgendaResponse();
        dto.setId(tarea.getId());
        dto.setMedicoNombre(tarea.getMedico().getNombreCompleto());
        dto.setTitulo(tarea.getTitulo());
        dto.setPrioridad(tarea.getPrioridad().name());
        dto.setFechaLimite(tarea.getFechaLimite());
        dto.setEstado(tarea.getEstado().name());
        return dto;
    }
}