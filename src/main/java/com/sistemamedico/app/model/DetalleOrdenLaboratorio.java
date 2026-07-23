package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

@Entity
@Table(name = "detalle_orden_laboratorio")
@SQLRestriction("eliminado = false")
public class DetalleOrdenLaboratorio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id")
    private OrdenLaboratorio orden;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "examen_id")
    private ExamenLaboratorio examen;

    public OrdenLaboratorio getOrden() { return orden; }
    public void setOrden(OrdenLaboratorio orden) { this.orden = orden; }
    public ExamenLaboratorio getExamen() { return examen; }
    public void setExamen(ExamenLaboratorio examen) { this.examen = examen; }
}