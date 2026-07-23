package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.SignosVitales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignosVitalesRepository extends JpaRepository<SignosVitales, Long> {
    Optional<SignosVitales> findByCitaId(Long citaId);
}