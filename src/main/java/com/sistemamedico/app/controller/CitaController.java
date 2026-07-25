package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.CitaRequest;
import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.model.Cita;
import com.sistemamedico.app.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaResponse> crear(@Valid @RequestBody CitaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{dpi}")
    public ResponseEntity<List<CitaResponse>> buscarPorPaciente(@PathVariable String dpi) {
        return ResponseEntity.ok(citaService.buscarPorPaciente(dpi));
    }

    @GetMapping
    public ResponseEntity<List<CitaResponse>> listar() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    // Cambiar estado: PAGADA, CANCELADA, ATENDIDA, etc.
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaResponse> cambiarEstado(@PathVariable Long id, @RequestParam Cita.EstadoCita nuevoEstado) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        citaService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}