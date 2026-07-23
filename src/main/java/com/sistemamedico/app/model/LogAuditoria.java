package com.sistemamedico.app.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId; // quien realizo la accion

    @Column(nullable = false, length = 100)
    private String entidad; // ej: "Usuario", "Cita", "ResultadoLaboratorio"

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Accion accion;

    @Column(name = "datos_anteriores", columnDefinition = "TEXT")
    private String datosAnteriores; // JSON, null si es CREATE

    @Column(name = "datos_nuevos", columnDefinition = "TEXT")
    private String datosNuevos; // JSON, null si es DELETE

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(length = 45)
    private String ip;

    public enum Accion {
        CREATE, UPDATE, DELETE
    }

    // --- Getters y setters (sin setId, es autogenerado; sin setters de negocio que permitan editar tras crear) ---
    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }
    public Long getEntidadId() { return entidadId; }
    public void setEntidadId(Long entidadId) { this.entidadId = entidadId; }
    public Accion getAccion() { return accion; }
    public void setAccion(Accion accion) { this.accion = accion; }
    public String getDatosAnteriores() { return datosAnteriores; }
    public void setDatosAnteriores(String datosAnteriores) { this.datosAnteriores = datosAnteriores; }
    public String getDatosNuevos() { return datosNuevos; }
    public void setDatosNuevos(String datosNuevos) { this.datosNuevos = datosNuevos; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}