package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.RolRequest;
import com.sistemamedico.app.dto.RolResponse;
import com.sistemamedico.app.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    public ResponseEntity<RolResponse> crear(@Valid @RequestBody RolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<RolResponse>> listar() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizar(@PathVariable Long id, @Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        rolService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}