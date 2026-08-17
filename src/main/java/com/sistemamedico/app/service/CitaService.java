package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.CitaRequest;
import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional; // Importar Optional
import java.util.stream.Collectors;

@Service
public class CitaService {

    private static final Logger logger = LoggerFactory.getLogger(CitaService.class);

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

        // Usar findByIdWithEagerRelations para cargar la especialidad y sucursal del médico
        Usuario medico = usuarioRepository.findByIdWithEagerRelations(request.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado."));

        if (!"Medico".equalsIgnoreCase(medico.getRol().getNombre())) {
            throw new IllegalArgumentException("El usuario seleccionado no tiene rol de Médico.");
        }
        
        // Validar que el médico tenga una especialidad asignada
        if (medico.getEspecialidad() == null) {
            throw new IllegalArgumentException("El médico seleccionado no tiene una especialidad asignada.");
        }

        Especialidad especialidad = especialidadRepository.findById(request.getEspecialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada."));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        if (!medico.getEspecialidad().getId().equals(especialidad.getId())) {
            throw new IllegalArgumentException("El médico seleccionado no pertenece a la especialidad indicada.");
        }

        boolean disponibleEnSede = sedeEspecialidadRepository
                .existsBySucursalIdAndEspecialidadId(sucursal.getId(), especialidad.getId());
        if (!disponibleEnSede) {
            throw new IllegalArgumentException("La especialidad seleccionada no está disponible en esta sucursal.");
        }

        // Verificar disponibilidad de horario real
        List<LocalTime> horariosOcupados = buscarHorariosOcupados(medico.getId(), request.getFechaHora().toLocalDate(), Optional.empty());
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
        // Definir los estados "activos" que queremos mostrar
        List<Cita.EstadoCita> estadosActivos = Arrays.asList(
                Cita.EstadoCita.RESERVADA,
                Cita.EstadoCita.PENDIENTE_PAGO,
                Cita.EstadoCita.PAGADA,
                Cita.EstadoCita.ATENDIDA,
                Cita.EstadoCita.REAGENDADA, // Incluir citas reagendadas
                Cita.EstadoCita.CANCELADA // Incluir citas canceladas
        );
        return citaRepository.findByPacienteDpiAndEstadoInOrderByIdDesc(dpi, estadosActivos).stream().map(this::mapearAResponse).toList();
    }

    public List<CitaResponse> listarTodas() {
        return citaRepository.findAllByOrderByIdDesc().stream().map(this::mapearAResponse).toList();
    }

    // Nuevo método para buscar horarios ocupados de un médico en una fecha, con opción de excluir una cita
    public List<LocalTime> buscarHorariosOcupados(Long medicoId, LocalDate fecha, Optional<Long> excludeCitaId) {
        LocalDateTime startOfDay = fecha.atStartOfDay();
        LocalDateTime endOfDay = fecha.atTime(LocalTime.MAX); // Fin del día

        List<Cita> citasEnElDia;
        if (excludeCitaId.isPresent()) {
            citasEnElDia = citaRepository.findByMedicoIdAndFechaHoraBetweenAndIdNot(medicoId, startOfDay, endOfDay, excludeCitaId.get());
        } else {
            citasEnElDia = citaRepository.findByMedicoIdAndFechaHoraBetween(medicoId, startOfDay, endOfDay);
        }

        return citasEnElDia.stream()
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

    @Transactional
    public void cancelarCita(Long citaId, Long pacienteId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));

        // Verificar que la cita pertenece al paciente que intenta cancelarla
        if (!cita.getPaciente().getId().equals(pacienteId)) {
            throw new IllegalArgumentException("No tiene permisos para cancelar esta cita.");
        }

        // Permitir cancelar citas en estado RESERVADA o REAGENDADA
        if (cita.getEstado() != Cita.EstadoCita.RESERVADA && cita.getEstado() != Cita.EstadoCita.REAGENDADA) {
            throw new IllegalArgumentException("Solo se pueden cancelar citas en estado RESERVADA o REAGENDADA.");
        }

        // Verificar que la cita sea futura
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden cancelar citas pasadas.");
        }

        cita.setEstado(Cita.EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    @Transactional
    public CitaResponse reagendarCita(Long originalCitaId, CitaRequest newCitaRequest, Long pacienteId) {
        // 1. Buscar la cita original
        Cita citaOriginal = citaRepository.findById(originalCitaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita original no encontrada."));

        // 2. Validar que la cita original pertenezca al paciente
        if (!citaOriginal.getPaciente().getId().equals(pacienteId)) {
            throw new IllegalArgumentException("No tiene permisos para reagendar esta cita.");
        }

        // 3. Validar que la cita original esté en estado reagendable (RESERVADA) y sea futura
        if (citaOriginal.getEstado() != Cita.EstadoCita.RESERVADA) {
            throw new IllegalArgumentException("Solo se pueden reagendar citas en estado RESERVADA.");
        }
        if (citaOriginal.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden reagendar citas pasadas.");
        }

        // 4. Verificar disponibilidad para la NUEVA fecha/hora, EXCLUYENDO la cita original
        List<LocalTime> horariosOcupados = buscarHorariosOcupados(newCitaRequest.getMedicoId(), newCitaRequest.getFechaHora().toLocalDate(), Optional.of(originalCitaId));
        if (horariosOcupados.contains(newCitaRequest.getFechaHora().toLocalTime())) {
            throw new IllegalArgumentException("El médico ya tiene una cita reservada en el nuevo horario.");
        }

        // 5. Actualizar los campos de la cita original con los nuevos datos
        // Reutilizamos la lógica de crear para obtener las entidades actualizadas
        Usuario nuevoMedico = usuarioRepository.findByIdWithEagerRelations(newCitaRequest.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Nuevo médico no encontrado."));
        Especialidad nuevaEspecialidad = especialidadRepository.findById(newCitaRequest.getEspecialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Nueva especialidad no encontrada."));
        Sucursal nuevaSucursal = sucursalRepository.findById(newCitaRequest.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Nueva sucursal no encontrada."));

        citaOriginal.setMedico(nuevoMedico);
        citaOriginal.setEspecialidad(nuevaEspecialidad);
        citaOriginal.setSucursal(nuevaSucursal);
        citaOriginal.setFechaHora(newCitaRequest.getFechaHora());
        citaOriginal.setMotivoVisita(newCitaRequest.getMotivoVisita());
        citaOriginal.setEstado(Cita.EstadoCita.REAGENDADA); // Establecer el estado a REAGENDADA

        logger.info("Reagendando cita con ID: {}", citaOriginal.getId()); // Log para verificar el ID

        Cita guardada = citaRepository.save(citaOriginal);

        // Opcional: Enviar notificación de reagendamiento
        notificacionService.enviar(
                "Cita Reagendada",
                citaOriginal.getPaciente().getCorreo(),
                "Tu cita ha sido reagendada",
                "Hola " + citaOriginal.getPaciente().getNombreCompleto() + ",\n\n" +
                        "Tu cita ha sido reagendada exitosamente.\n" +
                        "Medico: " + nuevoMedico.getNombreCompleto() + "\n" +
                        "Especialidad: " + nuevaEspecialidad.getNombre() + "\n" +
                        "Nueva Fecha y hora: " + newCitaRequest.getFechaHora() + "\n" +
                        "Sucursal: " + nuevaSucursal.getNombre() + "\n\n" +
                        "Sistema Medico 2026"
        );

        return mapearAResponse(guardada);
    }


    private CitaResponse mapearAResponse(Cita cita) {
        CitaResponse dto = new CitaResponse();
        dto.setId(cita.getId());
        dto.setPacienteNombre(cita.getPaciente().getNombreCompleto());
        dto.setPacienteDpi(cita.getPaciente().getDpi());
        dto.setMedicoId(cita.getMedico().getId()); // Añadido
        dto.setMedicoNombre(cita.getMedico().getNombreCompleto());
        dto.setEspecialidadId(cita.getEspecialidad().getId()); // Añadido
        dto.setEspecialidadNombre(cita.getEspecialidad().getEspecialidadNombre()); // Corregido
        dto.setSucursalId(cita.getSucursal().getId()); // Añadido
        dto.setSucursalNombre(cita.getSucursal().getNombre());
        dto.setFechaHora(cita.getFechaHora());
        dto.setMotivoVisita(cita.getMotivoVisita());
        dto.setEstado(cita.getEstado().name());
        dto.setTipo(cita.getTipo().name());
        dto.setVerificada(cita.isVerificada());
        return dto;
    }
}