package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.DetalleReceta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Long> {
    List<DetalleReceta> findByRecetaId(Long recetaId);
}