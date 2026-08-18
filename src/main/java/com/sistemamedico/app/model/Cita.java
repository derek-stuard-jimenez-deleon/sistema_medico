package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@SQLRestriction("eliminado = false")
public class Cita extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id")
    private Usuario medico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "motivo_visita", nullable = false, length = 2000)
    private String motivoVisita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoCita estado = EstadoCita.RESERVADA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCita tipo = TipoCita.NORMAL;

    // Si es cita de seguimiento (CU-11), referencia a la cita original
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_origen_id")
    private Cita citaOrigen;

    @Column(name = "tipo_seguimiento", length = 50)
    private String tipoSeguimiento; // "Monitoreo de condicion" / "Revision de resultados"

    // Reserva temporal (RNF-019, default 5 min) mientras se completa el pago
    @Column(name = "reserva_expira_en")
    private LocalDateTime reservaExpiraEn;

    @Column(name = "verificada")
    private boolean verificada = false;

    public enum EstadoCita {
        RESERVADA, PENDIENTE_PAGO, PAGADA, CANCELADA, ATENDIDA, REAGENDADA, ELIMINADA, PENDIENTE_CONSULTA_EMERGENCIA // <-- AÑADIDO
    }

    public Boolean isVerificada() { return verificada; }
    public void setVerificada(Boolean verificada) { this.verificada = verificada; }

    public enum TipoCita {
        NORMAL, SEGUIMIENTO
    }

    // --- Getters y setters ---
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Usuario getMedico() { return medico; }
    public void setMedico(Usuario medico) { this.medico = medico; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivoVisita() { return motivoVisita; }
    public void setMotivoVisita(String motivoVisita) { this.motivoVisita = motivoVisita; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public TipoCita getTipo() { return tipo; }
    public void setTipo(TipoCita tipo) { this.tipo = tipo; }
    public Cita getCitaOrigen() { return citaOrigen; }
    public void setCitaOrigen(Cita citaOrigen) { this.citaOrigen = citaOrigen; }
    public String getTipoSeguimiento() { return tipoSeguimiento; }
    public void setTipoSeguimiento(String tipoSeguimiento) { this.tipoSeguimiento = tipoSeguimiento; }
    public LocalDateTime getReservaExpiraEn() { return reservaExpiraEn; }
    public void setReservaExpiraEn(LocalDateTime reservaExpiraEn) { this.reservaExpiraEn = reservaExpiraEn; }
}