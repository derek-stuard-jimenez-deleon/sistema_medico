package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByMedicamentoIdAndSucursalIdOrderByFechaCreacionDesc(Long medicamentoId, Long sucursalId);
    
    @Query("SELECT m FROM MovimientoInventario m JOIN FETCH m.medicamento JOIN FETCH m.sucursal JOIN FETCH m.usuario ORDER BY m.fechaCreacion DESC")
    List<MovimientoInventario> findAllWithDetails();
}