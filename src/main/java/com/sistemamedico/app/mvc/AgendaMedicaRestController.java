package com.sistemamedico.app.mvc;

import com.sistemamedico.app.model.Cita;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.CitaRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agenda")
public class AgendaMedicaRestController {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendaMedicaRestController(CitaRepository citaRepository, UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/eventos")
    public List<Map<String, Object>> obtenerEventos(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Authentication authentication) {

        Usuario medico = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asegurarse de que el doctor solo vea sus citas (o si es admin, quiza pueda ver todas, 
        // pero el CU-16 dice administrar "su" agenda personal).
        List<Cita> citas = citaRepository.findByMedicoIdAndFechaHoraBetween(medico.getId(), start, end);

        List<Map<String, Object>> eventos = new ArrayList<>();

        for (Cita cita : citas) {
            Map<String, Object> evento = new HashMap<>();
            evento.put("id", cita.getId());
            evento.put("title", cita.getPaciente().getNombreCompleto() + " - " + cita.getEspecialidad().getNombre());
            evento.put("start", cita.getFechaHora().toString());
            // Asumimos 1 hora por cita de momento
            evento.put("end", cita.getFechaHora().plusMinutes(60).toString());
            
            // Determinar color por estado
            String color = "#3788d8"; // Default blue
            switch (cita.getEstado()) {
                case ATENDIDA:
                    color = "#198754"; // Success Green
                    break;
                case CANCELADA:
                case ELIMINADA:
                    color = "#dc3545"; // Danger Red
                    break;
                case EN_ESPERA_CONSULTA:
                case PENDIENTE_PAGO:
                case PAGADA:
                case PACIENTE_PRESENTE:
                case EN_SIGNOS_VITALES:
                    color = "#ffc107"; // Warning Yellow
                    evento.put("textColor", "#000");
                    break;
                case RESERVADA:
                case REAGENDADA:
                    color = "#0dcaf0"; // Info Cyan
                    break;
            }
            evento.put("color", color);
            
            // Info extra para mostrar en el popup
            evento.put("estado", cita.getEstado().name());
            evento.put("sucursal", cita.getSucursal().getNombre());
            evento.put("paciente", cita.getPaciente().getNombreCompleto());
            
            eventos.add(evento);
        }

        return eventos;
    }
}
