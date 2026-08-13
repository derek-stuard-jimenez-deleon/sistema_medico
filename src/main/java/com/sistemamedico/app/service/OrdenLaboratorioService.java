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
public class OrdenLaboratorioService {

    private final OrdenLaboratorioRepository ordenLaboratorioRepository;
    private final DetalleOrdenLaboratorioRepository detalleOrdenLaboratorioRepository;
    private final ResultadoLaboratorioRepository resultadoLaboratorioRepository;
    private final ConsultaMedicaRepository consultaMedicaRepository;
    private final ExamenLaboratorioRepository examenLaboratorioRepository;
    private final UsuarioRepository usuarioRepository;

    public OrdenLaboratorioService(OrdenLaboratorioRepository ordenLaboratorioRepository,
                                   DetalleOrdenLaboratorioRepository detalleOrdenLaboratorioRepository,
                                   ResultadoLaboratorioRepository resultadoLaboratorioRepository,
                                   ConsultaMedicaRepository consultaMedicaRepository,
                                   ExamenLaboratorioRepository examenLaboratorioRepository,
                                   UsuarioRepository usuarioRepository) {
        this.ordenLaboratorioRepository = ordenLaboratorioRepository;
        this.detalleOrdenLaboratorioRepository = detalleOrdenLaboratorioRepository;
        this.resultadoLaboratorioRepository = resultadoLaboratorioRepository;
        this.consultaMedicaRepository = consultaMedicaRepository;
        this.examenLaboratorioRepository = examenLaboratorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public OrdenLaboratorioResponse crear(OrdenLaboratorioRequest request) {
        ConsultaMedica consulta = consultaMedicaRepository.findById(request.getConsultaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Consulta médica no encontrada."));

        List<ExamenLaboratorio> examenes = request.getExamenesIds().stream()
                .map(id -> examenLaboratorioRepository.findById(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Examen de laboratorio no encontrado: " + id)))
                .toList();

        BigDecimal total = examenes.stream()
                .map(ExamenLaboratorio::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrdenLaboratorio orden = new OrdenLaboratorio();
        orden.setConsulta(consulta);
        orden.setPaciente(consulta.getCita().getPaciente());
        orden.setMedico(consulta.getMedico());
        orden.setEstado(OrdenLaboratorio.EstadoOrden.PENDIENTE);
        orden.setMontoTotal(total);
        OrdenLaboratorio ordenGuardada = ordenLaboratorioRepository.save(orden);

        List<DetalleOrdenLaboratorio> detalles = examenes.stream().map(examen -> {
            DetalleOrdenLaboratorio detalle = new DetalleOrdenLaboratorio();
            detalle.setOrden(ordenGuardada);
            detalle.setExamen(examen);
            return detalleOrdenLaboratorioRepository.save(detalle);
        }).toList();

        return mapearAResponse(ordenGuardada, detalles);
    }

    public OrdenLaboratorioResponse buscarPorId(Long id) {
        OrdenLaboratorio orden = ordenLaboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de laboratorio no encontrada."));
        List<DetalleOrdenLaboratorio> detalles = detalleOrdenLaboratorioRepository.findByOrdenId(orden.getId());
        return mapearAResponse(orden, detalles);
    }

    public List<OrdenLaboratorioResponse> buscarPendientesPorDpi(String dpi) {
        return ordenLaboratorioRepository.findByPacienteDpiAndEstado(dpi, OrdenLaboratorio.EstadoOrden.PENDIENTE)
                .stream()
                .map(orden -> mapearAResponse(orden, detalleOrdenLaboratorioRepository.findByOrdenId(orden.getId())))
                .toList();
    }

    public OrdenLaboratorioResponse buscarPendientePorId(Long id) {
        OrdenLaboratorio orden = ordenLaboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de laboratorio no encontrada."));
        if (orden.getEstado() != OrdenLaboratorio.EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("La orden indicada no está pendiente de pago.");
        }
        return mapearAResponse(orden, detalleOrdenLaboratorioRepository.findByOrdenId(orden.getId()));
    }

    // Registrar un resultado individual (dentro del detalle de una orden)
    @Transactional
    public ResultadoLaboratorioResponse registrarResultado(ResultadoLaboratorioRequest request) {
        DetalleOrdenLaboratorio detalle = detalleOrdenLaboratorioRepository.findById(request.getDetalleOrdenId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Detalle de orden no encontrado."));

        Usuario validador = usuarioRepository.findById(request.getValidadoPorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario validador no encontrado."));

        ExamenLaboratorio examen = detalle.getExamen();
        boolean fueraDeRango = false;
        if (examen.getRangoReferenciaMin() != null && examen.getRangoReferenciaMax() != null) {
            fueraDeRango = request.getValor().compareTo(examen.getRangoReferenciaMin()) < 0
                    || request.getValor().compareTo(examen.getRangoReferenciaMax()) > 0;
        }

        ResultadoLaboratorio resultado = new ResultadoLaboratorio();
        resultado.setDetalleOrden(detalle);
        resultado.setValor(request.getValor());
        resultado.setUnidadMedida(examen.getUnidadMedida());
        resultado.setFueraDeRango(fueraDeRango);
        resultado.setValidadoPor(validador);
        resultado.setPublicado(true);

        ResultadoLaboratorio guardado = resultadoLaboratorioRepository.save(resultado);

        // Si todos los detalles de la orden ya tienen resultado, marcamos la orden como Completada
        OrdenLaboratorio orden = detalle.getOrden();
        List<DetalleOrdenLaboratorio> todosLosDetalles = detalleOrdenLaboratorioRepository.findByOrdenId(orden.getId());
        boolean todosCompletos = todosLosDetalles.stream()
                .allMatch(d -> resultadoLaboratorioRepository.findByDetalleOrdenId(d.getId()).isPresent());
        if (todosCompletos) {
            orden.setEstado(OrdenLaboratorio.EstadoOrden.COMPLETADA);
            ordenLaboratorioRepository.save(orden);
        }

        ResultadoLaboratorioResponse dto = new ResultadoLaboratorioResponse();
        dto.setId(guardado.getId());
        dto.setExamenNombre(examen.getNombre());
        dto.setValor(guardado.getValor());
        dto.setUnidadMedida(guardado.getUnidadMedida());
        dto.setFueraDeRango(guardado.isFueraDeRango());
        dto.setValidadoPorNombre(validador.getNombreCompleto());
        dto.setPublicado(guardado.isPublicado());
        return dto;
    }

    private OrdenLaboratorioResponse mapearAResponse(OrdenLaboratorio orden, List<DetalleOrdenLaboratorio> detalles) {
        OrdenLaboratorioResponse dto = new OrdenLaboratorioResponse();
        dto.setId(orden.getId());
        dto.setPacienteNombre(orden.getPaciente().getNombreCompleto());
        dto.setPacienteDpi(orden.getPaciente().getDpi());
        dto.setMedicoNombre(orden.getMedico().getNombreCompleto());
        dto.setEstado(orden.getEstado().name());
        dto.setMontoTotal(orden.getMontoTotal());
        dto.setFechaCreacion(orden.getFechaCreacion());
        dto.setDetalles(detalles.stream().map(det -> {
            DetalleOrdenLaboratorioResponse d = new DetalleOrdenLaboratorioResponse();
            d.setId(det.getId());
            d.setExamenNombre(det.getExamen().getNombre());
            return d;
        }).toList());
        return dto;
    }
}
