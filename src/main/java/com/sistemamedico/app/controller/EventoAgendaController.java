package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.EventoAgendaRequest;
import com.sistemamedico.app.dto.EventoAgendaResponse;
import com.sistemamedico.app.service.EventoAgendaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/eventos-agenda")
public class EventoAgendaController {

    private final EventoAgendaService eventoAgendaService;

    public EventoAgendaController(EventoAgendaService eventoAgendaService) {
        this.eventoAgendaService = eventoAgendaService;
    }

    @PostMapping
    public ResponseEntity<EventoAgendaResponse> crear(@Valid @RequestBody EventoAgendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoAgendaService.crear(request));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<EventoAgendaResponse>> listarPorMedicoYRango(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(eventoAgendaService.listarPorMedicoYRango(medicoId, desde, hasta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        eventoAgendaService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}