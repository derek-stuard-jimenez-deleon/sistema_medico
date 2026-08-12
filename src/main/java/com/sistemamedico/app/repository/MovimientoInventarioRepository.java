package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByMedicamentoIdAndSucursalIdOrderByFechaCreacionDesc(Long medicamentoId, Long sucursalId);
    List<MovimientoInventario> findAllByOrderByFechaCreacionDesc();
}