package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.NotificacionResponse;
import com.sistemamedico.app.model.Notificacion;
import com.sistemamedico.app.repository.NotificacionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionRepository notificacionRepository;

    public NotificacionController(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @GetMapping
    public List<NotificacionResponse> listarPorEstado(
            @RequestParam(defaultValue = "PENDIENTE") Notificacion.EstadoNotificacion estado) {
        return notificacionRepository.findByEstado(estado).stream().map(n -> {
            NotificacionResponse dto = new NotificacionResponse();
            dto.setId(n.getId());
            dto.setTipo(n.getTipo());
            dto.setDestinatario(n.getDestinatario());
            dto.setAsunto(n.getAsunto());
            dto.setEstado(n.getEstado().name());
            dto.setFechaEnvio(n.getFechaEnvio());
            dto.setIntentosEnvio(n.getIntentosEnvio());
            return dto;
        }).toList();
    }
}