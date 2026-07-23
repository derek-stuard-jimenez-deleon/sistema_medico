package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "ordenes_laboratorio")
@SQLRestriction("eliminado = false")
public class OrdenLaboratorio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id")
    private ConsultaMedica consulta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id")
    private Usuario medico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    public enum EstadoOrden {
        PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA
    }

    // --- Getters y setters ---
    public ConsultaMedica getConsulta() { return consulta; }
    public void setConsulta(ConsultaMedica consulta) { this.consulta = consulta; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Usuario getMedico() { return medico; }
    public void setMedico(Usuario medico) { this.medico = medico; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
}