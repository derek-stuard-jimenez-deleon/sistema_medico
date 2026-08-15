package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteDpi(String dpi);
    List<Cita> findByMedicoIdAndEstado(Long medicoId, Cita.EstadoCita estado);
    List<Cita> findByEstado(Cita.EstadoCita estado);

    // Nuevo método para buscar citas de un médico en un rango de fechas (un día)
    List<Cita> findByMedicoIdAndFechaHoraBetween(Long medicoId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}