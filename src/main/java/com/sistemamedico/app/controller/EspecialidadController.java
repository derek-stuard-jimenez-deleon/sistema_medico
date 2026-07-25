package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.EspecialidadRequest;
import com.sistemamedico.app.dto.EspecialidadResponse;
import com.sistemamedico.app.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponse> crear(@Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponse>> listar() {
        return ResponseEntity.ok(especialidadService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponse> actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.ok(especialidadService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        especialidadService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}