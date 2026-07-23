package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.EventoAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoAgendaRepository extends JpaRepository<EventoAgenda, Long> {
    List<EventoAgenda> findByMedicoIdAndFechaInicioBetween(Long medicoId, LocalDateTime desde, LocalDateTime hasta);
}