package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.RolRequest;
import com.sistemamedico.app.dto.RolResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Rol;
import com.sistemamedico.app.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional
    public RolResponse crear(RolRequest request) {
        if (rolRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre " + request.getNombre() + ".");
        }
        Rol rol = new Rol();
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        rol.setActivo(request.isActivo());
        return mapearAResponse(rolRepository.save(rol));
    }

    public RolResponse buscarPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        return mapearAResponse(rol);
    }

    public List<RolResponse> listarTodos() {
        return rolRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id")).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public RolResponse actualizar(Long id, RolRequest request) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        rol.setActivo(request.isActivo());
        return mapearAResponse(rolRepository.save(rol));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        rol.marcarEliminado(usuarioQueElimina);
        rolRepository.save(rol);
    }

    private RolResponse mapearAResponse(Rol rol) {
        RolResponse dto = new RolResponse();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        dto.setActivo(rol.isActivo());
        return dto;
    }
}