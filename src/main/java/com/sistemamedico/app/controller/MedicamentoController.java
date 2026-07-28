package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.MedicamentoRequest;
import com.sistemamedico.app.dto.MedicamentoResponse;
import com.sistemamedico.app.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @PostMapping
    public ResponseEntity<MedicamentoResponse> crear(@Valid @RequestBody MedicamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicamentoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicamentoResponse>> listar() {
        return ResponseEntity.ok(medicamentoService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody MedicamentoRequest request) {
        return ResponseEntity.ok(medicamentoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        medicamentoService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}