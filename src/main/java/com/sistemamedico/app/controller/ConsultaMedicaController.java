package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.ConsultaMedicaRequest;
import com.sistemamedico.app.dto.ConsultaMedicaResponse;
import com.sistemamedico.app.service.ConsultaMedicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas-medicas")
public class ConsultaMedicaController {

    private final ConsultaMedicaService consultaMedicaService;

    public ConsultaMedicaController(ConsultaMedicaService consultaMedicaService) {
        this.consultaMedicaService = consultaMedicaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaMedicaResponse> crear(@Valid @RequestBody ConsultaMedicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaMedicaService.crear(request));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<ConsultaMedicaResponse> buscarPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(consultaMedicaService.buscarPorCita(citaId));
    }
}