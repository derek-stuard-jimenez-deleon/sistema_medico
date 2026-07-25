package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.*;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecetaMedicaService {

    private final RecetaMedicaRepository recetaMedicaRepository;
    private final DetalleRecetaRepository detalleRecetaRepository;
    private final ConsultaMedicaRepository consultaMedicaRepository;
    private final MedicamentoRepository medicamentoRepository;

    public RecetaMedicaService(RecetaMedicaRepository recetaMedicaRepository,
                               DetalleRecetaRepository detalleRecetaRepository,
                               ConsultaMedicaRepository consultaMedicaRepository,
                               MedicamentoRepository medicamentoRepository) {
        this.recetaMedicaRepository = recetaMedicaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.consultaMedicaRepository = consultaMedicaRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public RecetaMedicaResponse crear(RecetaMedicaRequest request) {
        ConsultaMedica consulta = consultaMedicaRepository.findById(request.getConsultaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Consulta médica no encontrada."));

        if (recetaMedicaRepository.findByConsultaId(consulta.getId()).stream().findAny().isPresent()) {
            throw new IllegalArgumentException("Esta consulta ya tiene una receta registrada.");
        }

        RecetaMedica receta = new RecetaMedica();
        receta.setConsulta(consulta);
        RecetaMedica recetaGuardada = recetaMedicaRepository.save(receta);

        List<DetalleReceta> detallesGuardados = request.getDetalles().stream().map(det -> {
            Medicamento medicamento = medicamentoRepository.findById(det.getMedicamentoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado."));

            DetalleReceta detalle = new DetalleReceta();
            detalle.setReceta(recetaGuardada);
            detalle.setMedicamento(medicamento);
            detalle.setDosis(det.getDosis());
            detalle.setFrecuencia(det.getFrecuencia());
            detalle.setDuracion(det.getDuracion());
            detalle.setIndicacionesEspeciales(det.getIndicacionesEspeciales());
            return detalleRecetaRepository.save(detalle);
        }).toList();

        return mapearAResponse(recetaGuardada, detallesGuardados);
    }

    public RecetaMedicaResponse buscarPorConsulta(Long consultaId) {
        List<RecetaMedica> recetas = recetaMedicaRepository.findByConsultaId(consultaId);
        if (recetas.isEmpty()) {
            throw new RecursoNoEncontradoException("No hay receta registrada para esta consulta.");
        }
        RecetaMedica receta = recetas.get(0);
        List<DetalleReceta> detalles = detalleRecetaRepository.findByRecetaId(receta.getId());
        return mapearAResponse(receta, detalles);
    }

    private RecetaMedicaResponse mapearAResponse(RecetaMedica receta, List<DetalleReceta> detalles) {
        RecetaMedicaResponse dto = new RecetaMedicaResponse();
        dto.setId(receta.getId());
        dto.setConsultaId(receta.getConsulta().getId());
        dto.setPacienteNombre(receta.getConsulta().getCita().getPaciente().getNombreCompleto());
        dto.setMedicoNombre(receta.getConsulta().getMedico().getNombreCompleto());
        dto.setDetalles(detalles.stream().map(det -> {
            DetalleRecetaResponse d = new DetalleRecetaResponse();
            d.setId(det.getId());
            d.setMedicamentoNombre(det.getMedicamento().getNombre());
            d.setDosis(det.getDosis());
            d.setFrecuencia(det.getFrecuencia());
            d.setDuracion(det.getDuracion());
            d.setIndicacionesEspeciales(det.getIndicacionesEspeciales());
            return d;
        }).toList());
        return dto;
    }
}