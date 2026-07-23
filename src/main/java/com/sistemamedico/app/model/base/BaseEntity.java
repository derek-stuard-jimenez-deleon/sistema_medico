package com.sistemamedico.app.model.base;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad base para TODO el sistema.
 * Aporta: llave primaria, auditoria (quien crea/modifica y cuando)
 * y borrado logico (nunca se hace DELETE fisico).
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Auditoria ---
    @Column(name = "creado_por", updatable = false)
    private Long creadoPor; // id del Usuario que creo el registro

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "modificado_por")
    private Long modificadoPor; // id del Usuario que hizo el ultimo cambio

    @UpdateTimestamp
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    // --- Borrado logico ---
    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;

    @Column(name = "eliminado_por")
    private Long eliminadoPor;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    // Control de concurrencia optimista (util en inventario y otros)
    @Version
    @Column(name = "row_version")
    private Long rowVersion;

    // --- Getters y setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Long creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public Long getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(Long modificadoPor) { this.modificadoPor = modificadoPor; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }

    public boolean isEliminado() { return eliminado; }

    public Long getEliminadoPor() { return eliminadoPor; }

    public LocalDateTime getFechaEliminacion() { return fechaEliminacion; }

    public Long getRowVersion() { return rowVersion; }

    /** Marca el registro como eliminado sin borrarlo fisicamente. */
    public void marcarEliminado(Long usuarioId) {
        this.eliminado = true;
        this.eliminadoPor = usuarioId;
        this.fechaEliminacion = LocalDateTime.now();
    }
}