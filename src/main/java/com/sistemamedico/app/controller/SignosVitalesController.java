package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.SignosVitalesRequest;
import com.sistemamedico.app.dto.SignosVitalesResponse;
import com.sistemamedico.app.service.SignosVitalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signos-vitales")
public class SignosVitalesController {

    private final SignosVitalesService signosVitalesService;

    public SignosVitalesController(SignosVitalesService signosVitalesService) {
        this.signosVitalesService = signosVitalesService;
    }

    @PostMapping
    public ResponseEntity<SignosVitalesResponse> crearSignosVitales(@Valid @RequestBody SignosVitalesRequest request) {
        SignosVitalesResponse response = signosVitalesService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignosVitalesResponse> buscarPorId(@PathVariable Long id) {
        SignosVitalesResponse response = signosVitalesService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<SignosVitalesResponse> buscarPorCita(@PathVariable Long citaId) {
        SignosVitalesResponse response = signosVitalesService.buscarPorCita(citaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SignosVitalesResponse>> listarTodos() {
        List<SignosVitalesResponse> response = signosVitalesService.listarTodos();
        return ResponseEntity.ok(response);
    }

    // Puedes añadir más endpoints como PUT, DELETE, etc.
}