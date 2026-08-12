package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.*;
import com.sistemamedico.app.service.OrdenLaboratorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes-laboratorio")
public class OrdenLaboratorioController {

    private final OrdenLaboratorioService ordenLaboratorioService;

    public OrdenLaboratorioController(OrdenLaboratorioService ordenLaboratorioService) {
        this.ordenLaboratorioService = ordenLaboratorioService;
    }

    @PostMapping
    public ResponseEntity<OrdenLaboratorioResponse> crear(@Valid @RequestBody OrdenLaboratorioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenLaboratorioService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenLaboratorioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenLaboratorioService.buscarPorId(id));
    }

    @GetMapping("/pendientes/{dpi}")
    public ResponseEntity<List<OrdenLaboratorioResponse>> buscarPendientesPorDpi(@PathVariable String dpi) {
        return ResponseEntity.ok(ordenLaboratorioService.buscarPendientesPorDpi(dpi));
    }

    @PostMapping("/resultados")
    public ResponseEntity<ResultadoLaboratorioResponse> registrarResultado(@Valid @RequestBody ResultadoLaboratorioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenLaboratorioService.registrarResultado(request));
    }
}