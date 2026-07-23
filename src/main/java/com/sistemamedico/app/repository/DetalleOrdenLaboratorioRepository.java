package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.DetalleOrdenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleOrdenLaboratorioRepository extends JpaRepository<DetalleOrdenLaboratorio, Long> {
    List<DetalleOrdenLaboratorio> findByOrdenId(Long ordenId);
}