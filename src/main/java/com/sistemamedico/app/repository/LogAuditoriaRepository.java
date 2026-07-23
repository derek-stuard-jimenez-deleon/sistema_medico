package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    List<LogAuditoria> findByEntidadAndEntidadId(String entidad, Long entidadId);
    List<LogAuditoria> findByUsuarioId(Long usuarioId);
}