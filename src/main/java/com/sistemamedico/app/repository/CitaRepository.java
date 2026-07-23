package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteDpi(String dpi);
    List<Cita> findByMedicoIdAndEstado(Long medicoId, Cita.EstadoCita estado);
    List<Cita> findByEstado(Cita.EstadoCita estado);
}