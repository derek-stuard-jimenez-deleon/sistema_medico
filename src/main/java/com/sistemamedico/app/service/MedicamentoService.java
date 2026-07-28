package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.MedicamentoRequest;
import com.sistemamedico.app.dto.MedicamentoResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Medicamento;
import com.sistemamedico.app.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public MedicamentoResponse crear(MedicamentoRequest request) {
        if (medicamentoRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un medicamento con el nombre " + request.getNombre() + ".");
        }
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre(request.getNombre());
        medicamento.setDescripcion(request.getDescripcion());
        medicamento.setPrecio(request.getPrecio());
        medicamento.setControlado(request.isControlado());
        medicamento.setActivo(request.isActivo());
        return mapearAResponse(medicamentoRepository.save(medicamento));
    }

    public MedicamentoResponse buscarPorId(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado."));
        return mapearAResponse(medicamento);
    }

    public List<MedicamentoResponse> listarTodos() {
        return medicamentoRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public MedicamentoResponse actualizar(Long id, MedicamentoRequest request) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado."));
        medicamento.setNombre(request.getNombre());
        medicamento.setDescripcion(request.getDescripcion());
        medicamento.setPrecio(request.getPrecio());
        medicamento.setControlado(request.isControlado());
        medicamento.setActivo(request.isActivo());
        return mapearAResponse(medicamentoRepository.save(medicamento));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado."));
        medicamento.marcarEliminado(usuarioQueElimina);
        medicamentoRepository.save(medicamento);
    }

    private MedicamentoResponse mapearAResponse(Medicamento medicamento) {
        MedicamentoResponse dto = new MedicamentoResponse();
        dto.setId(medicamento.getId());
        dto.setNombre(medicamento.getNombre());
        dto.setDescripcion(medicamento.getDescripcion());
        dto.setPrecio(medicamento.getPrecio());
        dto.setControlado(medicamento.isControlado());
        dto.setActivo(medicamento.isActivo());
        return dto;
    }
}