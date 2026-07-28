package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.ExamenLaboratorioRequest;
import com.sistemamedico.app.dto.ExamenLaboratorioResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.ExamenLaboratorio;
import com.sistemamedico.app.repository.ExamenLaboratorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamenLaboratorioService {

    private final ExamenLaboratorioRepository examenLaboratorioRepository;

    public ExamenLaboratorioService(ExamenLaboratorioRepository examenLaboratorioRepository) {
        this.examenLaboratorioRepository = examenLaboratorioRepository;
    }

    @Transactional
    public ExamenLaboratorioResponse crear(ExamenLaboratorioRequest request) {
        ExamenLaboratorio examen = new ExamenLaboratorio();
        examen.setNombre(request.getNombre());
        examen.setPrecio(request.getPrecio());
        examen.setUnidadMedida(request.getUnidadMedida());
        examen.setRangoReferenciaMin(request.getRangoReferenciaMin());
        examen.setRangoReferenciaMax(request.getRangoReferenciaMax());
        examen.setActivo(request.isActivo());
        return mapearAResponse(examenLaboratorioRepository.save(examen));
    }

    public ExamenLaboratorioResponse buscarPorId(Long id) {
        ExamenLaboratorio examen = examenLaboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Examen de laboratorio no encontrado."));
        return mapearAResponse(examen);
    }

    public List<ExamenLaboratorioResponse> listarTodos() {
        return examenLaboratorioRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        ExamenLaboratorio examen = examenLaboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Examen de laboratorio no encontrado."));
        examen.marcarEliminado(usuarioQueElimina);
        examenLaboratorioRepository.save(examen);
    }

    private ExamenLaboratorioResponse mapearAResponse(ExamenLaboratorio examen) {
        ExamenLaboratorioResponse dto = new ExamenLaboratorioResponse();
        dto.setId(examen.getId());
        dto.setNombre(examen.getNombre());
        dto.setPrecio(examen.getPrecio());
        dto.setUnidadMedida(examen.getUnidadMedida());
        dto.setRangoReferenciaMin(examen.getRangoReferenciaMin());
        dto.setRangoReferenciaMax(examen.getRangoReferenciaMax());
        dto.setActivo(examen.isActivo());
        return dto;
    }
}