package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.TareaAgendaRequest;
import com.sistemamedico.app.dto.TareaAgendaResponse;
import com.sistemamedico.app.model.TareaAgenda;
import com.sistemamedico.app.service.TareaAgendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas-agenda")
public class TareaAgendaController {

    private final TareaAgendaService tareaAgendaService;

    public TareaAgendaController(TareaAgendaService tareaAgendaService) {
        this.tareaAgendaService = tareaAgendaService;
    }

    @PostMapping
    public ResponseEntity<TareaAgendaResponse> crear(@Valid @RequestBody TareaAgendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaAgendaService.crear(request));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<TareaAgendaResponse>> listarPorMedicoYEstado(
            @PathVariable Long medicoId,
            @RequestParam(defaultValue = "PENDIENTE") TareaAgenda.EstadoTarea estado) {
        return ResponseEntity.ok(tareaAgendaService.listarPorMedicoYEstado(medicoId, estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TareaAgendaResponse> cambiarEstado(@PathVariable Long id, @RequestParam TareaAgenda.EstadoTarea nuevoEstado) {
        return ResponseEntity.ok(tareaAgendaService.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        tareaAgendaService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}