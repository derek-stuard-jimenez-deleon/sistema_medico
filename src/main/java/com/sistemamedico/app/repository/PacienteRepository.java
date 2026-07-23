package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDpi(String dpi);

    Optional<Paciente> findByUsername(String username);

    boolean existsByDpi(String dpi);

    boolean existsByCorreo(String correo);
}