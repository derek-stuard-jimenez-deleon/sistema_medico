package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.SucursalRequest;
import com.sistemamedico.app.dto.SucursalResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Sucursal;
import com.sistemamedico.app.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public SucursalResponse crear(SucursalRequest request) {
        if (sucursalRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre " + request.getNombre() + ".");
        }
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setHorarioAtencion(request.getHorarioAtencion());
        sucursal.setActivo(request.isActivo());
        return mapearAResponse(sucursalRepository.save(sucursal));
    }

    public SucursalResponse buscarPorId(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));
        return mapearAResponse(sucursal);
    }

    public List<SucursalResponse> listarTodos() {
        return sucursalRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id")).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public SucursalResponse actualizar(Long id, SucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setHorarioAtencion(request.getHorarioAtencion());
        sucursal.setActivo(request.isActivo());
        return mapearAResponse(sucursalRepository.save(sucursal));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));
        sucursal.marcarEliminado(usuarioQueElimina);
        sucursalRepository.save(sucursal);
    }

    private SucursalResponse mapearAResponse(Sucursal sucursal) {
        SucursalResponse dto = new SucursalResponse();
        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());
        dto.setTelefono(sucursal.getTelefono());
        dto.setHorarioAtencion(sucursal.getHorarioAtencion());
        dto.setActivo(sucursal.isActivo());
        return dto;
    }
}