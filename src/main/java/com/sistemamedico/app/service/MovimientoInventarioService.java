package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.MovimientoInventarioResponse;
import com.sistemamedico.app.model.MovimientoInventario;
import com.sistemamedico.app.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listarTodos() {
        return movimientoInventarioRepository.findAllWithDetails()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private MovimientoInventarioResponse mapearAResponse(MovimientoInventario movimiento) {
        MovimientoInventarioResponse dto = new MovimientoInventarioResponse();
        dto.setId(movimiento.getId());
        dto.setTipo(movimiento.getTipoMovimiento().name()); // Corregido
        dto.setMedicamentoNombre(movimiento.getMedicamento().getNombre());
        dto.setSucursalNombre(movimiento.getSucursal().getNombre());
        dto.setCantidad(movimiento.getCantidad());
        dto.setStockAnterior(movimiento.getStockAnterior());
        dto.setStockNuevo(movimiento.getStockNuevo());
        dto.setReferencia(movimiento.getReferencia());
        dto.setUsuarioNombre(movimiento.getUsuario().getNombreCompleto());
        dto.setFechaCreacion(movimiento.getFechaCreacion());
        return dto;
    }
}