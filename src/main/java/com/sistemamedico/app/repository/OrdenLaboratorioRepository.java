package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.OrdenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenLaboratorioRepository extends JpaRepository<OrdenLaboratorio, Long> {
    List<OrdenLaboratorio> findByPacienteDpi(String dpi);
    List<OrdenLaboratorio> findByEstado(OrdenLaboratorio.EstadoOrden estado);
    List<OrdenLaboratorio> findByPacienteDpiAndEstado(String dpi, OrdenLaboratorio.EstadoOrden estado);
}