package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.ConsultaMedicaRequest;
import com.sistemamedico.app.dto.ConsultaMedicaResponse;
import com.sistemamedico.app.service.ConsultaMedicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas-medicas")
public class ConsultaMedicaController {

    private final ConsultaMedicaService consultaMedicaService;

    public ConsultaMedicaController(ConsultaMedicaService consultaMedicaService) {
        this.consultaMedicaService = consultaMedicaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaMedicaResponse> crearConsulta(@Valid @RequestBody ConsultaMedicaRequest request) {
        ConsultaMedicaResponse response = consultaMedicaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaMedicaResponse> buscarPorId(@PathVariable Long id) {
        ConsultaMedicaResponse response = consultaMedicaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<ConsultaMedicaResponse> buscarPorCita(@PathVariable Long citaId) {
        ConsultaMedicaResponse response = consultaMedicaService.buscarPorCita(citaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ConsultaMedicaResponse>> listarTodas() {
        List<ConsultaMedicaResponse> response = consultaMedicaService.listarTodas();
        return ResponseEntity.ok(response);
    }

    // Puedes añadir más endpoints como PUT, DELETE, etc.
}