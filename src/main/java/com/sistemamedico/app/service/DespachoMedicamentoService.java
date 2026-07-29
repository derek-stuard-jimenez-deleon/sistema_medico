package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.*;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DespachoMedicamentoService {

    private final DespachoMedicamentoRepository despachoRepository;
    private final DetalleDespachoRepository detalleDespachoRepository;
    private final RecetaMedicaRepository recetaMedicaRepository;
    private final DetalleRecetaRepository detalleRecetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final InventarioMedicamentoRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public DespachoMedicamentoService(DespachoMedicamentoRepository despachoRepository,
                                      DetalleDespachoRepository detalleDespachoRepository,
                                      RecetaMedicaRepository recetaMedicaRepository,
                                      DetalleRecetaRepository detalleRecetaRepository,
                                      UsuarioRepository usuarioRepository,
                                      SucursalRepository sucursalRepository,
                                      InventarioMedicamentoRepository inventarioRepository,
                                      MovimientoInventarioRepository movimientoInventarioRepository) {
        this.despachoRepository = despachoRepository;
        this.detalleDespachoRepository = detalleDespachoRepository;
        this.recetaMedicaRepository = recetaMedicaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.sucursalRepository = sucursalRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    @Transactional
    public DespachoMedicamentoResponse crear(DespachoMedicamentoRequest request) {
        RecetaMedica receta = recetaMedicaRepository.findById(request.getRecetaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Receta no encontrada."));

        Usuario farmaceutico = usuarioRepository.findById(request.getFarmaceuticoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Farmacéutico no encontrado."));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        List<DetalleReceta> detallesReceta = detalleRecetaRepository.findByRecetaId(receta.getId());
        if (detallesReceta.isEmpty()) {
            throw new IllegalArgumentException("La receta no tiene medicamentos registrados.");
        }

        Paciente paciente = receta.getConsulta().getCita().getPaciente();

        DespachoMedicamento despacho = new DespachoMedicamento();
        despacho.setReceta(receta);
        despacho.setPaciente(paciente);
        despacho.setFarmaceutico(farmaceutico);
        despacho.setEstado(DespachoMedicamento.EstadoDespacho.DESPACHADO);
        DespachoMedicamento despachoGuardado = despachoRepository.save(despacho);

        List<DetalleDespacho> detallesDespacho = detallesReceta.stream().map(detReceta -> {
            Medicamento medicamento = detReceta.getMedicamento();

            InventarioMedicamento inventario = inventarioRepository
                    .findByMedicamentoIdAndSucursalId(medicamento.getId(), sucursal.getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No hay inventario registrado para " + medicamento.getNombre() + " en esta sucursal."));

            int cantidad = 1; // simplificado: 1 unidad por medicamento recetado
            if (inventario.getStockActual() < cantidad) {
                throw new IllegalArgumentException("Stock insuficiente de " + medicamento.getNombre() + ".");
            }

            // Descuenta el inventario
            inventario.setStockActual(inventario.getStockActual() - cantidad);
            inventarioRepository.save(inventario);

            // Registra el movimiento en la bitacora (RN-CU13-01)
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setMedicamento(medicamento);
            movimiento.setSucursal(sucursal);
            movimiento.setTipoMovimiento(MovimientoInventario.TipoMovimiento.SALIDA_AJUSTE);
            movimiento.setCantidad(cantidad);
            movimiento.setMotivo("Despacho de receta #" + receta.getId());
            movimiento.setUsuario(farmaceutico);
            movimientoInventarioRepository.save(movimiento);

            DetalleDespacho detalleDespacho = new DetalleDespacho();
            detalleDespacho.setDespacho(despachoGuardado);
            detalleDespacho.setMedicamento(medicamento);
            detalleDespacho.setCantidad(cantidad);
            detalleDespacho.setPrecioUnitario(medicamento.getPrecio());
            return detalleDespachoRepository.save(detalleDespacho);
        }).toList();

        return mapearAResponse(despachoGuardado, detallesDespacho);
    }

    public DespachoMedicamentoResponse buscarPorId(Long id) {
        DespachoMedicamento despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Despacho no encontrado."));
        List<DetalleDespacho> detalles = detalleDespachoRepository.findByDespachoId(despacho.getId());
        return mapearAResponse(despacho, detalles);
    }

    private DespachoMedicamentoResponse mapearAResponse(DespachoMedicamento despacho, List<DetalleDespacho> detalles) {
        DespachoMedicamentoResponse dto = new DespachoMedicamentoResponse();
        dto.setId(despacho.getId());
        dto.setPacienteNombre(despacho.getPaciente().getNombreCompleto());
        dto.setFarmaceuticoNombre(despacho.getFarmaceutico().getNombreCompleto());
        dto.setEstado(despacho.getEstado().name());
        dto.setDetalles(detalles.stream().map(det -> {
            DetalleDespachoResponse d = new DetalleDespachoResponse();
            d.setId(det.getId());
            d.setMedicamentoNombre(det.getMedicamento().getNombre());
            d.setCantidad(det.getCantidad());
            d.setPrecioUnitario(det.getPrecioUnitario());
            return d;
        }).toList());
        return dto;
    }
}