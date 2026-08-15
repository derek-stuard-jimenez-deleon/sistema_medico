package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.CitaRequest;
import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadRepository especialidadRepository;
    private final SucursalRepository sucursalRepository;
    private final SedeEspecialidadRepository sedeEspecialidadRepository;
    private final NotificacionService notificacionService;

    public CitaService(CitaRepository citaRepository,
                       PacienteRepository pacienteRepository,
                       UsuarioRepository usuarioRepository,
                       EspecialidadRepository especialidadRepository,
                       SucursalRepository sucursalRepository,
                       SedeEspecialidadRepository sedeEspecialidadRepository,
                       NotificacionService notificacionService) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.especialidadRepository = especialidadRepository;
        this.sucursalRepository = sucursalRepository;
        this.sedeEspecialidadRepository = sedeEspecialidadRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public CitaResponse crear(CitaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado."));

        Usuario medico = usuarioRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado."));

        if (!"Medico".equalsIgnoreCase(medico.getRol().getNombre())) {
            throw new IllegalArgumentException("El usuario seleccionado no tiene rol de Médico.");
        }

        Especialidad especialidad = especialidadRepository.findById(request.getEspecialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        if (medico.getEspecialidad() == null || !medico.getEspecialidad().getId().equals(especialidad.getId())) {
            throw new IllegalArgumentException("El médico seleccionado no pertenece a la especialidad indicada.");
        }

        boolean disponibleEnSede = sedeEspecialidadRepository
                .existsBySucursalIdAndEspecialidadId(sucursal.getId(), especialidad.getId());
        if (!disponibleEnSede) {
            throw new IllegalArgumentException("La especialidad seleccionada no está disponible en esta sucursal.");
        }

        // Verificar disponibilidad de horario real
        List<LocalTime> horariosOcupados = buscarHorariosOcupados(medico.getId(), request.getFechaHora().toLocalDate());
        if (horariosOcupados.contains(request.getFechaHora().toLocalTime())) {
            throw new IllegalArgumentException("El médico ya tiene una cita reservada en ese horario.");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);
        cita.setSucursal(sucursal);
        cita.setFechaHora(request.getFechaHora());
        cita.setMotivoVisita(request.getMotivoVisita());
        cita.setEstado(Cita.EstadoCita.RESERVADA);
        cita.setTipo(Cita.TipoCita.NORMAL);

        Cita guardada = citaRepository.save(cita);

        notificacionService.enviar(
                "Confirmacion Cita",
                paciente.getCorreo(),
                "Confirmacion de tu cita medica",
                "Hola " + paciente.getNombreCompleto() + ",\n\n" +
                        "Tu cita ha sido reservada exitosamente.\n" +
                        "Medico: " + medico.getNombreCompleto() + "\n" +
                        "Especialidad: " + especialidad.getNombre() + "\n" +
                        "Fecha y hora: " + request.getFechaHora() + "\n" +
                        "Sucursal: " + sucursal.getNombre() + "\n\n" +
                        "Por favor, realiza el pago para confirmar tu cita.\n\n" +
                        "Sistema Medico 2026"
        );

        return mapearAResponse(guardada);
    }

    public CitaResponse buscarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));
        return mapearAResponse(cita);
    }

    public List<CitaResponse> buscarPorPaciente(String dpi) {
        return citaRepository.findByPacienteDpi(dpi).stream().map(this::mapearAResponse).toList();
    }

    public List<CitaResponse> listarTodas() {
        return citaRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    // Nuevo método para buscar horarios ocupados de un médico en una fecha
    public List<LocalTime> buscarHorariosOcupados(Long medicoId, LocalDate fecha) {
        LocalDateTime startOfDay = fecha.atStartOfDay();
        LocalDateTime endOfDay = fecha.atTime(LocalTime.MAX); // Fin del día

        return citaRepository.findByMedicoIdAndFechaHoraBetween(medicoId, startOfDay, endOfDay)
                .stream()
                .map(cita -> cita.getFechaHora().toLocalTime())
                .collect(Collectors.toList());
    }

    @Transactional
    public CitaResponse cambiarEstado(Long id, Cita.EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));
        cita.setEstado(nuevoEstado);
        return mapearAResponse(citaRepository.save(cita));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioQueElimina) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));
        cita.marcarEliminado(usuarioQueElimina);
        citaRepository.save(cita);
    }

    @Transactional
    public CitaResponse marcarVerificada(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));

        if (cita.getEstado() == Cita.EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("No se puede verificar una cita cancelada.");
        }

        cita.setVerificada(true);
        return mapearAResponse(citaRepository.save(cita));
    }

    private CitaResponse mapearAResponse(Cita cita) {
        CitaResponse dto = new CitaResponse();
        dto.setId(cita.getId());
        dto.setPacienteNombre(cita.getPaciente().getNombreCompleto());
        dto.setPacienteDpi(cita.getPaciente().getDpi());
        dto.setMedicoNombre(cita.getMedico().getNombreCompleto());
        dto.setEspecialidadNombre(cita.getEspecialidad().getNombre());
        dto.setSucursalNombre(cita.getSucursal().getNombre());
        dto.setFechaHora(cita.getFechaHora());
        dto.setMotivoVisita(cita.getMotivoVisita());
        dto.setEstado(cita.getEstado().name());
        dto.setTipo(cita.getTipo().name());
        dto.setVerificada(cita.isVerificada());
        return dto;
    }
}