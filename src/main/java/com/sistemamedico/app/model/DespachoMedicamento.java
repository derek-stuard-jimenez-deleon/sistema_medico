package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

@Entity
@Table(name = "despachos_medicamento")
@SQLRestriction("eliminado = false")
public class DespachoMedicamento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receta_id")
    private RecetaMedica receta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmaceutico_id")
    private Usuario farmaceutico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDespacho estado = EstadoDespacho.PENDIENTE;

    public enum EstadoDespacho {
        PENDIENTE, DESPACHADO, CANCELADO
    }

    public RecetaMedica getReceta() { return receta; }
    public void setReceta(RecetaMedica receta) { this.receta = receta; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Usuario getFarmaceutico() { return farmaceutico; }
    public void setFarmaceutico(Usuario farmaceutico) { this.farmaceutico = farmaceutico; }
    public EstadoDespacho getEstado() { return estado; }
    public void setEstado(EstadoDespacho estado) { this.estado = estado; }
}