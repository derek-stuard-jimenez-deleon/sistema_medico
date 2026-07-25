package com.sistemamedico.app.controller;

import com.sistemamedico.app.dto.UsuarioRequest;
import com.sistemamedico.app.dto.UsuarioResponse;
import com.sistemamedico.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse creado = usuarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // RN-CU01-02: 20 registros por pagina por defecto
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano);

        Page<UsuarioResponse> resultado = (nombre != null && !nombre.isBlank())
                ? usuarioService.buscarPorNombre(nombre, pageable)
                : usuarioService.listarTodos(pageable);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    // Borrado logico. El usuarioId de quien elimina se pasa como query param
    // temporalmente -- cuando tengamos login real (JWT), esto va a venir
    // automaticamente del usuario autenticado, no de un parametro manual.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long usuarioQueElimina) {
        usuarioService.eliminar(id, usuarioQueElimina);
        return ResponseEntity.noContent().build();
    }
}