package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.DetalleDespacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleDespachoRepository extends JpaRepository<DetalleDespacho, Long> {
    List<DetalleDespacho> findByDespachoId(Long despachoId);
}