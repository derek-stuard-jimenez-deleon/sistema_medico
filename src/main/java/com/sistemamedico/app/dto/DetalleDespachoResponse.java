package com.sistemamedico.app.dto;

import java.math.BigDecimal;

public class DetalleDespachoResponse {

    private Long id;
    private String medicamentoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicamentoNombre() { return medicamentoNombre; }
    public void setMedicamentoNombre(String medicamentoNombre) { this.medicamentoNombre = medicamentoNombre; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}