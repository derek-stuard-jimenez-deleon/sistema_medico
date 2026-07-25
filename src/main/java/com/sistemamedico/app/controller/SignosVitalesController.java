package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.SignosVitalesRequest;
import com.sistemamedico.app.dto.SignosVitalesResponse;
import com.sistemamedico.app.service.SignosVitalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signos-vitales")
public class SignosVitalesController {

    private final SignosVitalesService signosVitalesService;

    public SignosVitalesController(SignosVitalesService signosVitalesService) {
        this.signosVitalesService = signosVitalesService;
    }

    @PostMapping
    public ResponseEntity<SignosVitalesResponse> crear(@Valid @RequestBody SignosVitalesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signosVitalesService.crear(request));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<SignosVitalesResponse> buscarPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(signosVitalesService.buscarPorCita(citaId));
    }
}