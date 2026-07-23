package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.ExamenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamenLaboratorioRepository extends JpaRepository<ExamenLaboratorio, Long> {
    List<ExamenLaboratorio> findByActivoTrue();
}