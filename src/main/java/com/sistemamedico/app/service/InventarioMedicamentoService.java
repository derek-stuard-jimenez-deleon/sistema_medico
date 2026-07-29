package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.InventarioMedicamentoRequest;
import com.sistemamedico.app.dto.InventarioMedicamentoResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.InventarioMedicamento;
import com.sistemamedico.app.model.Medicamento;
import com.sistemamedico.app.model.Sucursal;
import com.sistemamedico.app.repository.InventarioMedicamentoRepository;
import com.sistemamedico.app.repository.MedicamentoRepository;
import com.sistemamedico.app.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioMedicamentoService {

    private final InventarioMedicamentoRepository inventarioRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final SucursalRepository sucursalRepository;

    public InventarioMedicamentoService(InventarioMedicamentoRepository inventarioRepository,
                                        MedicamentoRepository medicamentoRepository,
                                        SucursalRepository sucursalRepository) {
        this.inventarioRepository = inventarioRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public InventarioMedicamentoResponse crear(InventarioMedicamentoRequest request) {
        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado."));
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        if (inventarioRepository.findByMedicamentoIdAndSucursalId(medicamento.getId(), sucursal.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un registro de inventario para este medicamento en esta sucursal.");
        }

        InventarioMedicamento inventario = new InventarioMedicamento();
        inventario.setMedicamento(medicamento);
        inventario.setSucursal(sucursal);
        inventario.setStockActual(request.getStockActual());
        inventario.setStockMinimo(request.getStockMinimo());

        return mapearAResponse(inventarioRepository.save(inventario));
    }

    public List<InventarioMedicamentoResponse> listarPorSucursal(Long sucursalId) {
        return inventarioRepository.findBySucursalId(sucursalId).stream().map(this::mapearAResponse).toList();
    }

    private InventarioMedicamentoResponse mapearAResponse(InventarioMedicamento inventario) {
        InventarioMedicamentoResponse dto = new InventarioMedicamentoResponse();
        dto.setId(inventario.getId());
        dto.setMedicamentoNombre(inventario.getMedicamento().getNombre());
        dto.setSucursalNombre(inventario.getSucursal().getNombre());
        dto.setStockActual(inventario.getStockActual());
        dto.setStockMinimo(inventario.getStockMinimo());
        dto.setStockBajo(inventario.getStockActual() <= inventario.getStockMinimo());
        return dto;
    }
}