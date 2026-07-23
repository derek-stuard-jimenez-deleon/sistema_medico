package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "signos_vitales")
@SQLRestriction("eliminado = false")
public class SignosVitales extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enfermero_id")
    private Usuario enfermero;

    @Column(name = "presion_sistolica", nullable = false)
    private Integer presionSistolica; // 60-250 mmHg

    @Column(name = "presion_diastolica", nullable = false)
    private Integer presionDiastolica; // 40-150 mmHg

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal temperatura; // 34.0-42.0 C

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal peso; // 0.5-300 kg

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal talla; // 30-250 cm

    @Column(name = "frecuencia_cardiaca", nullable = false)
    private Integer frecuenciaCardiaca; // 30-220 lpm

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    // --- Getters y setters ---
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public Usuario getEnfermero() { return enfermero; }
    public void setEnfermero(Usuario enfermero) { this.enfermero = enfermero; }
    public Integer getPresionSistolica() { return presionSistolica; }
    public void setPresionSistolica(Integer presionSistolica) { this.presionSistolica = presionSistolica; }
    public Integer getPresionDiastolica() { return presionDiastolica; }
    public void setPresionDiastolica(Integer presionDiastolica) { this.presionDiastolica = presionDiastolica; }
    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getTalla() { return talla; }
    public void setTalla(BigDecimal talla) { this.talla = talla; }
    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}