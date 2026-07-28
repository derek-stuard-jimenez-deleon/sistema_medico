package com.sistemamedico.app.dto;

import java.math.BigDecimal;

public class ExamenLaboratorioResponse {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String unidadMedida;
    private BigDecimal rangoReferenciaMin;
    private BigDecimal rangoReferenciaMax;
    private boolean activo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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