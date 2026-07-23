package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.DespachoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespachoMedicamentoRepository extends JpaRepository<DespachoMedicamento, Long> {
    List<DespachoMedicamento> findByRecetaId(Long recetaId);
    List<DespachoMedicamento> findByPacienteId(Long pacienteId);
}