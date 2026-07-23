package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.TareaAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaAgendaRepository extends JpaRepository<TareaAgenda, Long> {
    List<TareaAgenda> findByMedicoIdAndEstado(Long medicoId, TareaAgenda.EstadoTarea estado);
}