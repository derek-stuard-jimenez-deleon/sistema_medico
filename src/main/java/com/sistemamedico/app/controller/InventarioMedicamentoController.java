package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.InventarioMedicamentoRequest;
import com.sistemamedico.app.dto.InventarioMedicamentoResponse;
import com.sistemamedico.app.service.InventarioMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario-medicamento")
public class InventarioMedicamentoController {

    private final InventarioMedicamentoService inventarioMedicamentoService;

    public InventarioMedicamentoController(InventarioMedicamentoService inventarioMedicamentoService) {
        this.inventarioMedicamentoService = inventarioMedicamentoService;
    }

    @PostMapping
    public ResponseEntity<InventarioMedicamentoResponse> crear(@Valid @RequestBody InventarioMedicamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioMedicamentoService.crear(request));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<InventarioMedicamentoResponse>> listarPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(inventarioMedicamentoService.listarPorSucursal(sucursalId));
    }
}