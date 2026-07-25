package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.SucursalRequest;
import com.sistemamedico.app.dto.SucursalResponse;
import com.sistemamedico.app.service.SucursalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @PostMapping
    public ResponseEntity<SucursalResponse> crear(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> listar() {
        return ResponseEntity.ok(sucursalService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        sucursalService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}