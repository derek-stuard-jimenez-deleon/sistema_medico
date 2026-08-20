package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.SedeEspecialidadRequest;
import com.sistemamedico.app.dto.SedeEspecialidadResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Especialidad;
import com.sistemamedico.app.model.SedeEspecialidad;
import com.sistemamedico.app.model.Sucursal;
import com.sistemamedico.app.repository.EspecialidadRepository;
import com.sistemamedico.app.repository.SedeEspecialidadRepository;
import com.sistemamedico.app.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SedeEspecialidadService {

    private final SedeEspecialidadRepository sedeEspecialidadRepository;
    private final SucursalRepository sucursalRepository;
    private final EspecialidadRepository especialidadRepository;

    public SedeEspecialidadService(SedeEspecialidadRepository sedeEspecialidadRepository,
                                   SucursalRepository sucursalRepository,
                                   EspecialidadRepository especialidadRepository) {
        this.sedeEspecialidadRepository = sedeEspecialidadRepository;
        this.sucursalRepository = sucursalRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    public SedeEspecialidadResponse crear(SedeEspecialidadRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));
        Especialidad especialidad = especialidadRepository.findById(request.getEspecialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));

        if (sedeEspecialidadRepository.existsBySucursalIdAndEspecialidadId(sucursal.getId(), especialidad.getId())) {
            throw new IllegalArgumentException("Esta especialidad ya está habilitada en esta sucursal.");
        }

        SedeEspecialidad sedeEspecialidad = new SedeEspecialidad();
        sedeEspecialidad.setSucursal(sucursal);
        sedeEspecialidad.setEspecialidad(especialidad);

        return mapearAResponse(sedeEspecialidadRepository.save(sedeEspecialidad));
    }

    public List<SedeEspecialidadResponse> listarPorSucursal(Long sucursalId) {
        return sedeEspecialidadRepository.findBySucursalId(sucursalId).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        SedeEspecialidad sedeEspecialidad = sedeEspecialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro no encontrado."));
        sedeEspecialidad.marcarEliminado(usuarioQueElimina);
        sedeEspecialidadRepository.save(sedeEspecialidad);
    }

    private SedeEspecialidadResponse mapearAResponse(SedeEspecialidad sedeEspecialidad) {
        SedeEspecialidadResponse dto = new SedeEspecialidadResponse();
        dto.setId(sedeEspecialidad.getId());
        dto.setSucursalNombre(sedeEspecialidad.getSucursal().getNombre());
        dto.setEspecialidadId(sedeEspecialidad.getEspecialidad().getId());
        dto.setEspecialidadNombre(sedeEspecialidad.getEspecialidad().getNombre());
        return dto;
    }
}