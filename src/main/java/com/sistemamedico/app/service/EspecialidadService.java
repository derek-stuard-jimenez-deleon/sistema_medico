package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.EspecialidadRequest;
import com.sistemamedico.app.dto.EspecialidadResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Especialidad;
import com.sistemamedico.app.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    @CacheEvict(value = "especialidades", allEntries = true)
    public EspecialidadResponse crear(EspecialidadRequest request) {
        if (especialidadRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una especialidad con el nombre " + request.getNombre() + ".");
        }
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(request.getNombre());
        especialidad.setDescripcion(request.getDescripcion());
        especialidad.setActivo(request.isActivo());
        return mapearAResponse(especialidadRepository.save(especialidad));
    }

    public EspecialidadResponse buscarPorId(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));
        return mapearAResponse(especialidad);
    }

    @Cacheable("especialidades")
    public List<EspecialidadResponse> listarTodos() {
        return especialidadRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    @CacheEvict(value = "especialidades", allEntries = true)
    public EspecialidadResponse actualizar(Long id, EspecialidadRequest request) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));
        especialidad.setNombre(request.getNombre());
        especialidad.setDescripcion(request.getDescripcion());
        especialidad.setActivo(request.isActivo());
        return mapearAResponse(especialidadRepository.save(especialidad));
    }

    @Transactional
    @CacheEvict(value = "especialidades", allEntries = true)
    public void eliminar(Long id, Long usuarioQueElimina) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));
        especialidad.marcarEliminado(usuarioQueElimina);
        especialidadRepository.save(especialidad);
    }

    private EspecialidadResponse mapearAResponse(Especialidad especialidad) {
        EspecialidadResponse dto = new EspecialidadResponse();
        dto.setId(especialidad.getId());
        dto.setNombre(especialidad.getNombre());
        dto.setDescripcion(especialidad.getDescripcion());
        dto.setActivo(especialidad.isActivo());
        return dto;
    }
}