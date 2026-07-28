package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.ExamenLaboratorioRequest;
import com.sistemamedico.app.dto.ExamenLaboratorioResponse;
import com.sistemamedico.app.service.ExamenLaboratorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examenes-laboratorio")
public class ExamenLaboratorioController {

    private final ExamenLaboratorioService examenLaboratorioService;

    public ExamenLaboratorioController(ExamenLaboratorioService examenLaboratorioService) {
        this.examenLaboratorioService = examenLaboratorioService;
    }

    @PostMapping
    public ResponseEntity<ExamenLaboratorioResponse> crear(@Valid @RequestBody ExamenLaboratorioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenLaboratorioService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ExamenLaboratorioResponse>> listar() {
        return ResponseEntity.ok(examenLaboratorioService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        examenLaboratorioService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}