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
    private final com.sistemamedico.app.repository.InventarioMedicamentoRepository inventarioRepository;
    private final com.sistemamedico.app.repository.MedicamentoRepository medicamentoRepository;
    private final com.sistemamedico.app.repository.SucursalRepository sucursalRepository;
    private final com.sistemamedico.app.repository.UsuarioRepository usuarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository,
                                       com.sistemamedico.app.repository.InventarioMedicamentoRepository inventarioRepository,
                                       com.sistemamedico.app.repository.MedicamentoRepository medicamentoRepository,
                                       com.sistemamedico.app.repository.SucursalRepository sucursalRepository,
                                       com.sistemamedico.app.repository.UsuarioRepository usuarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.inventarioRepository = inventarioRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.sucursalRepository = sucursalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listarTodos() {
        return movimientoInventarioRepository.findAllWithDetails()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void registrarAjuste(Long sucursalId, Long medicamentoId, MovimientoInventario.TipoMovimiento tipo, Integer cantidad, String referencia, String motivo, String username) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        var sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada."));
        var medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado."));
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        var inventario = inventarioRepository.findByMedicamentoIdAndSucursalId(medicamentoId, sucursalId)
                .orElseGet(() -> {
                    com.sistemamedico.app.model.InventarioMedicamento inv = new com.sistemamedico.app.model.InventarioMedicamento();
                    inv.setMedicamento(medicamento);
                    inv.setSucursal(sucursal);
                    inv.setStockActual(0);
                    return inventarioRepository.save(inv);
                });

        int stockAnterior = inventario.getStockActual();
        int stockNuevo = stockAnterior;

        // Entradas suman, salidas restan
        if (tipo == MovimientoInventario.TipoMovimiento.ENTRADA_COMPRA || tipo == MovimientoInventario.TipoMovimiento.AJUSTE_INVENTARIO_FISICO) {
            // Para ajuste fisico asumimos que la UI permite + o - pero el select separará. 
            // Si es ajuste, asumiremos que si es "ENTRADA" o "AJUSTE" suma. Si es un ajuste negativo, usaremos SALIDA_AJUSTE.
            stockNuevo = stockAnterior + cantidad;
        } else if (tipo == MovimientoInventario.TipoMovimiento.SALIDA_AJUSTE || tipo == MovimientoInventario.TipoMovimiento.VENTA || tipo == MovimientoInventario.TipoMovimiento.DESPACHO) {
            stockNuevo = stockAnterior - cantidad;
            if (stockNuevo < 0) {
                throw new IllegalArgumentException("Stock insuficiente para realizar este movimiento de salida.");
            }
        } else {
            // TRANSFERENCIA no implementado
            throw new IllegalArgumentException("Tipo de movimiento no soportado para ajuste manual.");
        }

        inventario.setStockActual(stockNuevo);
        inventarioRepository.save(inventario);

        MovimientoInventario mov = new MovimientoInventario();
        mov.setMedicamento(medicamento);
        mov.setSucursal(sucursal);
        mov.setTipoMovimiento(tipo);
        mov.setCantidad(cantidad);
        mov.setStockAnterior(stockAnterior);
        mov.setStockNuevo(stockNuevo);
        mov.setReferencia(referencia);
        mov.setMotivo(motivo);
        mov.setUsuario(usuario);

        movimientoInventarioRepository.save(mov);
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