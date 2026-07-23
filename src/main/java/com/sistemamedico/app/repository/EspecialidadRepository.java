package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    Optional<Especialidad> findByNombre(String nombre);
    List<Especialidad> findByActivoTrue();
}