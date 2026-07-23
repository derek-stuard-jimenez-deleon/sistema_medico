package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.RecetaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Long> {
    List<RecetaMedica> findByConsultaId(Long consultaId);
}