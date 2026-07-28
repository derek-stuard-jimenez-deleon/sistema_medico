package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class MedicamentoRequest {

    @NotBlank(message = "El nombre del medicamento es obligatorio.")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres.")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres.")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero.")
    private BigDecimal precio;

    private boolean controlado = false;
    private boolean activo = true;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isControlado() { return controlado; }
    public void setControlado(boolean controlado) { this.controlado = controlado; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}