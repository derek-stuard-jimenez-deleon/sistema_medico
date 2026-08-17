package com.sistemamedico.app.service;

import com.sistemamedico.app.model.Cita;
import com.sistemamedico.app.repository.CitaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CitaSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(CitaSchedulerService.class);
    private final CitaRepository citaRepository;

    public CitaSchedulerService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    /**
     * Tarea programada para eliminar citas en estado RESERVADA que no han sido pagadas
     * después de un tiempo determinado (ej. 5 minutos).
     * Se ejecuta cada minuto.
     */
    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS) // Ejecutar cada 20 SEGUNDOS
    @Transactional
    public void eliminarCitasExpiradas() { // Renombrado el método para reflejar la acción
        logger.info("Iniciando tarea programada: eliminación de citas expiradas.");
        LocalDateTime now = LocalDateTime.now();

        // Buscar citas en estado RESERVADA cuya reservaExpiraEn sea anterior al momento actual
        List<Cita> citasExpiradas = citaRepository.findByEstadoAndReservaExpiraEnBefore(Cita.EstadoCita.RESERVADA, now);

        if (citasExpiradas.isEmpty()) {
            logger.info("No se encontraron citas RESERVADAS expiradas para eliminar.");
            return;
        }

        logger.info("Se encontraron {} citas RESERVADAS expiradas para eliminar.", citasExpiradas.size());

        for (Cita cita : citasExpiradas) {
            citaRepository.delete(cita); // Eliminar la cita completamente
            logger.info("Cita {} (Médico: {}, Paciente: {}, Fecha: {}) ha sido ELIMINADA por expiración de tiempo de pago.",
                    cita.getId(), cita.getMedico().getNombreCompleto(), cita.getPaciente().getNombreCompleto(), cita.getFechaHora());
        }
        logger.info("Tarea programada finalizada: {} citas eliminadas.", citasExpiradas.size());
    }
}