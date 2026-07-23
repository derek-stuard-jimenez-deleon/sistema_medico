package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultadoLaboratorioRepository extends JpaRepository<ResultadoLaboratorio, Long> {
    Optional<ResultadoLaboratorio> findByDetalleOrdenId(Long detalleOrdenId);
    List<ResultadoLaboratorio> findByPublicadoFalse();
}