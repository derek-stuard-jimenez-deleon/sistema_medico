package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

@Entity
@Table(name = "consultas_medicas")
@SQLRestriction("eliminado = false")
public class ConsultaMedica extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id")
    private Usuario medico;

    @Column(name = "motivo_consulta", nullable = false, length = 2000)
    private String motivoConsulta;

    @Column(name = "hallazgos_clinicos", nullable = false, length = 5000)
    private String hallazgosClinicos;

    @Column(length = 5000)
    private String diagnostico; // obligatorio solo para CERRAR la consulta (RN-CU08-01)

    @Column(name = "codigo_cie10", length = 10)
    private String codigoCie10;

    @Column(name = "plan_tratamiento", nullable = false, length = 5000)
    private String planTratamiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoConsulta estado = EstadoConsulta.ABIERTA;

    public enum EstadoConsulta {
        ABIERTA, CERRADA
    }

    // --- Getters y setters ---
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public Usuario getMedico() { return medico; }
    public void setMedico(Usuario medico) { this.medico = medico; }
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    public String getHallazgosClinicos() { return hallazgosClinicos; }
    public void setHallazgosClinicos(String hallazgosClinicos) { this.hallazgosClinicos = hallazgosClinicos; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getCodigoCie10() { return codigoCie10; }
    public void setCodigoCie10(String codigoCie10) { this.codigoCie10 = codigoCie10; }
    public String getPlanTratamiento() { return planTratamiento; }
    public void setPlanTratamiento(String planTratamiento) { this.planTratamiento = planTratamiento; }
    public EstadoConsulta getEstado() { return estado; }
    public void setEstado(EstadoConsulta estado) { this.estado = estado; }
}