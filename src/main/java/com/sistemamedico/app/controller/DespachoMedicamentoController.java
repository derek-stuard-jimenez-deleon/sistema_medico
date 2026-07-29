package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.DespachoMedicamentoRequest;
import com.sistemamedico.app.dto.DespachoMedicamentoResponse;
import com.sistemamedico.app.service.DespachoMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/despachos-medicamento")
public class DespachoMedicamentoController {

    private final DespachoMedicamentoService despachoMedicamentoService;

    public DespachoMedicamentoController(DespachoMedicamentoService despachoMedicamentoService) {
        this.despachoMedicamentoService = despachoMedicamentoService;
    }

    @PostMapping
    public ResponseEntity<DespachoMedicamentoResponse> crear(@Valid @RequestBody DespachoMedicamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despachoMedicamentoService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespachoMedicamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(despachoMedicamentoService.buscarPorId(id));
    }
}