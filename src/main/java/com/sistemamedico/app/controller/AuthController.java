package com.sistemamedico.app.controller;

import com.sistemamedico.app.config.JwtUtil;
import com.sistemamedico.app.dto.LoginRequest;
import com.sistemamedico.app.dto.LoginResponse;
import com.sistemamedico.app.repository.PacienteRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository,
                          PacienteRepository pacienteRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }

        // Determina si es personal interno o paciente, y arma el token
        var usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
        if (usuarioOpt.isPresent()) {
            var u = usuarioOpt.get();
            String token = jwtUtil.generarToken(u.getUsername(), "USUARIO", u.getId());
            return ResponseEntity.ok(new LoginResponse(token, u.getUsername(), "USUARIO"));
        }

        var paciente = pacienteRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrectos."));
        String token = jwtUtil.generarToken(paciente.getUsername(), "PACIENTE", paciente.getId());
        return ResponseEntity.ok(new LoginResponse(token, paciente.getUsername(), "PACIENTE"));
    }
}