package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

@Entity
@Table(name = "detalle_receta")
@SQLRestriction("eliminado = false")
public class DetalleReceta extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receta_id")
    private RecetaMedica receta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

    @Column(nullable = false, length = 200)
    private String dosis;

    @Column(nullable = false, length = 200)
    private String frecuencia;

    @Column(nullable = false, length = 100)
    private String duracion;

    @Column(name = "indicaciones_especiales", length = 1000)
    private String indicacionesEspeciales; // opcional

    // --- Getters y setters ---
    public RecetaMedica getReceta() { return receta; }
    public void setReceta(RecetaMedica receta) { this.receta = receta; }
    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public String getIndicacionesEspeciales() { return indicacionesEspeciales; }
    public void setIndicacionesEspeciales(String indicacionesEspeciales) { this.indicacionesEspeciales = indicacionesEspeciales; }
}