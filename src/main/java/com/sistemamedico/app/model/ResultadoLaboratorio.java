package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_laboratorio")
@SQLRestriction("eliminado = false")
public class ResultadoLaboratorio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_orden_id")
    private DetalleOrdenLaboratorio detalleOrden;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "unidad_medida", length = 30)
    private String unidadMedida;

    @Column(name = "fuera_de_rango", nullable = false)
    private boolean fueraDeRango = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por_id")
    private Usuario validadoPor;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Column(nullable = false)
    private boolean publicado = false;

    public DetalleOrdenLaboratorio getDetalleOrden() { return detalleOrden; }
    public void setDetalleOrden(DetalleOrdenLaboratorio detalleOrden) { this.detalleOrden = detalleOrden; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public boolean isFueraDeRango() { return fueraDeRango; }
    public void setFueraDeRango(boolean fueraDeRango) { this.fueraDeRango = fueraDeRango; }
    public Usuario getValidadoPor() { return validadoPor; }
    public void setValidadoPor(Usuario validadoPor) { this.validadoPor = validadoPor; }
    public LocalDateTime getFechaValidacion() { return fechaValidacion; }
    public void setFechaValidacion(LocalDateTime fechaValidacion) { this.fechaValidacion = fechaValidacion; }
    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }
}