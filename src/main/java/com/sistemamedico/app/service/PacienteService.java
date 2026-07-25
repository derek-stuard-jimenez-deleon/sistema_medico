package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.PacienteRequest;
import com.sistemamedico.app.dto.PacienteResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Paciente;
import com.sistemamedico.app.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    public PacienteService(PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PacienteResponse crear(PacienteRequest request) {
        if (pacienteRepository.existsByDpi(request.getDpi())) {
            throw new IllegalArgumentException("El DPI ingresado ya se encuentra registrado.");
        }
        if (pacienteRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ingresado ya se encuentra registrado.");
        }

        Paciente paciente = new Paciente();
        paciente.setDpi(request.getDpi());
        paciente.setNombreCompleto(request.getNombreCompleto());
        paciente.setTelefono(request.getTelefono());
        paciente.setCorreo(request.getCorreo());
        paciente.setNumeroSeguro(request.getNumeroSeguro());
        paciente.setUsername(request.getUsername());
        paciente.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        paciente.setActivo(request.isActivo());

        Paciente guardado = pacienteRepository.save(paciente);
        return mapearAResponse(guardado);
    }

    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado."));
        return mapearAResponse(paciente);
    }

    public PacienteResponse buscarPorDpi(String dpi) {
        Paciente paciente = pacienteRepository.findByDpi(dpi)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado."));
        return mapearAResponse(paciente);
    }

    public Page<PacienteResponse> listarTodos(Pageable pageable) {
        return pacienteRepository.findAll(pageable).map(this::mapearAResponse);
    }

    @Transactional
    public PacienteResponse actualizar(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado."));

        paciente.setNombreCompleto(request.getNombreCompleto());
        paciente.setTelefono(request.getTelefono());
        paciente.setCorreo(request.getCorreo());
        paciente.setNumeroSeguro(request.getNumeroSeguro());
        paciente.setActivo(request.isActivo());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            paciente.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        Paciente actualizado = pacienteRepository.save(paciente);
        return mapearAResponse(actualizado);
    }

    // Borrado logico: NUNCA se hace delete fisico
    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado."));
        paciente.marcarEliminado(usuarioQueElimina);
        pacienteRepository.save(paciente);
    }

    private PacienteResponse mapearAResponse(Paciente paciente) {
        PacienteResponse dto = new PacienteResponse();
        dto.setId(paciente.getId());
        dto.setDpi(paciente.getDpi());
        dto.setNombreCompleto(paciente.getNombreCompleto());
        dto.setTelefono(paciente.getTelefono());
        dto.setCorreo(paciente.getCorreo());
        dto.setNumeroSeguro(paciente.getNumeroSeguro());
        dto.setUsername(paciente.getUsername());
        dto.setActivo(paciente.isActivo());
        return dto;
    }
}