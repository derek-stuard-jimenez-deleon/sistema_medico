package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    Optional<Medicamento> findByNombre(String nombre);
    List<Medicamento> findByActivoTrue();
}
