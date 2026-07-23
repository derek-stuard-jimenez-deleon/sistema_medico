package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByNumeroTransaccion(String numeroTransaccion);
    List<Pago> findByTipoOrigenAndReferenciaId(Pago.TipoOrigenPago tipoOrigen, Long referenciaId);
}