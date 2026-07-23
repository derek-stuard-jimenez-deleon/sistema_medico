package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.DocumentoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoCitaRepository extends JpaRepository<DocumentoCita, Long> {
    List<DocumentoCita> findByCitaId(Long citaId);
}
