package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByEstado(Notificacion.EstadoNotificacion estado);
}