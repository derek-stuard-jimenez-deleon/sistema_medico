package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.MovimientoInventarioResponse;
import com.sistemamedico.app.model.MovimientoInventario;
import com.sistemamedico.app.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    public List<MovimientoInventarioResponse> listarTodos() {
        return movimientoInventarioRepository.findAllByOrderByFechaCreacionDesc()
                .stream().map(this::mapearAResponse).toList();
    }

    public List<MovimientoInventarioResponse> listarPorMedicamentoYSucursal(Long medicamentoId, Long sucursalId) {
        return movimientoInventarioRepository
                .findByMedicamentoIdAndSucursalIdOrderByFechaCreacionDesc(medicamentoId, sucursalId)
                .stream().map(this::mapearAResponse).toList();
    }

    private MovimientoInventarioResponse mapearAResponse(MovimientoInventario movimiento) {
        MovimientoInventarioResponse dto = new MovimientoInventarioResponse();
        dto.setId(movimiento.getId());
        dto.setMedicamentoNombre(movimiento.getMedicamento().getNombre());
        dto.setSucursalNombre(movimiento.getSucursal().getNombre());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento().name());
        dto.setCantidad(movimiento.getCantidad());
        dto.setMotivo(movimiento.getMotivo());
        dto.setUsuarioNombre(movimiento.getUsuario().getNombreCompleto());
        dto.setFechaCreacion(movimiento.getFechaCreacion());
        return dto;
    }
}