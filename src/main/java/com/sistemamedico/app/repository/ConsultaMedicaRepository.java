package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.ConsultaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedica, Long> {
    Optional<ConsultaMedica> findByCitaId(Long citaId);
    List<ConsultaMedica> findByCitaPacienteId(Long pacienteId);
}