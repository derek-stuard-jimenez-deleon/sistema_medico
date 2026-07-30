package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.SedeEspecialidadRequest;
import com.sistemamedico.app.dto.SedeEspecialidadResponse;
import com.sistemamedico.app.service.SedeEspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sede-especialidad")
public class SedeEspecialidadController {

    private final SedeEspecialidadService sedeEspecialidadService;

    public SedeEspecialidadController(SedeEspecialidadService sedeEspecialidadService) {
        this.sedeEspecialidadService = sedeEspecialidadService;
    }

    @PostMapping
    public ResponseEntity<SedeEspecialidadResponse> crear(@Valid @RequestBody SedeEspecialidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sedeEspecialidadService.crear(request));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<SedeEspecialidadResponse>> listarPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(sedeEspecialidadService.listarPorSucursal(sucursalId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        sedeEspecialidadService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}