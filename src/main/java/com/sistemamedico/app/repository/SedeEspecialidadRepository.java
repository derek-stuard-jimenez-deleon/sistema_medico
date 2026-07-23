package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.SedeEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SedeEspecialidadRepository extends JpaRepository<SedeEspecialidad, Long> {
    List<SedeEspecialidad> findBySucursalId(Long sucursalId);
    boolean existsBySucursalIdAndEspecialidadId(Long sucursalId, Long especialidadId);
}