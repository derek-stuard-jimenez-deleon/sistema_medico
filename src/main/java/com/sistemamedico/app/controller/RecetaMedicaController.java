package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.RecetaMedicaRequest;
import com.sistemamedico.app.dto.RecetaMedicaResponse;
import com.sistemamedico.app.service.RecetaMedicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recetas-medicas")
public class RecetaMedicaController {

    private final RecetaMedicaService recetaMedicaService;

    public RecetaMedicaController(RecetaMedicaService recetaMedicaService) {
        this.recetaMedicaService = recetaMedicaService;
    }

    @PostMapping
    public ResponseEntity<RecetaMedicaResponse> crear(@Valid @RequestBody RecetaMedicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recetaMedicaService.crear(request));
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<RecetaMedicaResponse> buscarPorConsulta(@PathVariable Long consultaId) {
        return ResponseEntity.ok(recetaMedicaService.buscarPorConsulta(consultaId));
    }
}