package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.InventarioMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioMedicamentoRepository extends JpaRepository<InventarioMedicamento, Long> {
    Optional<InventarioMedicamento> findByMedicamentoIdAndSucursalId(Long medicamentoId, Long sucursalId);
    List<InventarioMedicamento> findBySucursalId(Long sucursalId);

    // Util para la alerta de stock minimo (RN-CU10-03)
    List<InventarioMedicamento> findByStockActualLessThanEqual(Integer stockMinimoComparado);

    @org.springframework.data.jpa.repository.Query("SELECT i FROM InventarioMedicamento i JOIN FETCH i.medicamento JOIN FETCH i.sucursal")
    List<InventarioMedicamento> findAllWithDetails();
}