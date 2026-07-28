package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ExamenLaboratorioRequest {

    @NotBlank(message = "El nombre del examen es obligatorio.")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres.")
    private String nombre;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero.")
    private BigDecimal precio;

    @Size(max = 30, message = "La unidad de medida no puede exceder 30 caracteres.")
    private String unidadMedida;

    private BigDecimal rangoReferenciaMin;
    private BigDecimal rangoReferenciaMax;
    private boolean activo = true;

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