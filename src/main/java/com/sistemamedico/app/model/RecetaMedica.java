package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

@Entity
@Table(name = "recetas_medicas")
@SQLRestriction("eliminado = false")
public class RecetaMedica extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id")
    private ConsultaMedica consulta;

    public ConsultaMedica getConsulta() { return consulta; }
    public void setConsulta(ConsultaMedica consulta) { this.consulta = consulta; }
}