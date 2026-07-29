package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.LogAuditoriaResponse;
import com.sistemamedico.app.repository.LogAuditoriaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log-auditoria")
public class LogAuditoriaController {

    private final LogAuditoriaRepository logAuditoriaRepository;

    public LogAuditoriaController(LogAuditoriaRepository logAuditoriaRepository) {
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    @GetMapping("/entidad/{entidad}/{entidadId}")
    public List<LogAuditoriaResponse> listarPorEntidad(@PathVariable String entidad, @PathVariable Long entidadId) {
        return logAuditoriaRepository.findByEntidadAndEntidadId(entidad, entidadId).stream().map(this::mapear).toList();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<LogAuditoriaResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return logAuditoriaRepository.findByUsuarioId(usuarioId).stream().map(this::mapear).toList();
    }

    private LogAuditoriaResponse mapear(com.sistemamedico.app.model.LogAuditoria log) {
        LogAuditoriaResponse dto = new LogAuditoriaResponse();
        dto.setId(log.getId());
        dto.setUsuarioId(log.getUsuarioId());
        dto.setEntidad(log.getEntidad());
        dto.setEntidadId(log.getEntidadId());
        dto.setAccion(log.getAccion().name());
        dto.setFechaHora(log.getFechaHora());
        return dto;
    }
}