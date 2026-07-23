package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "examenes_laboratorio")
@SQLRestriction("eliminado = false")
public class ExamenLaboratorio extends BaseEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "unidad_medida", length = 30)
    private String unidadMedida;

    @Column(name = "rango_referencia_min", precision = 10, scale = 2)
    private BigDecimal rangoReferenciaMin;

    @Column(name = "rango_referencia_max", precision = 10, scale = 2)
    private BigDecimal rangoReferenciaMax;

    @Column(nullable = false)
    private boolean activo = true;

    // --- Getters y setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public BigDecimal getRangoReferenciaMin() { return rangoReferenciaMin; }
    public void setRangoReferenciaMin(BigDecimal rangoReferenciaMin) { this.rangoReferenciaMin = rangoReferenciaMin; }
    public BigDecimal getRangoReferenciaMax() { return rangoReferenciaMax; }
    public void setRangoReferenciaMax(BigDecimal rangoReferenciaMax) { this.rangoReferenciaMax = rangoReferenciaMax; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}