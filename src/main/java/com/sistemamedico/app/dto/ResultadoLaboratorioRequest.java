package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ResultadoLaboratorioRequest {

    @NotNull(message = "Debe indicar el detalle de la orden.")
    private Long detalleOrdenId;

    @NotNull(message = "El valor del resultado es obligatorio.")
    private BigDecimal valor;

    @NotNull(message = "Debe indicar el usuario que valida el resultado.")
    private Long validadoPorId;

    public Long getDetalleOrdenId() { return detalleOrdenId; }
    public void setDetalleOrdenId(Long detalleOrdenId) { this.detalleOrdenId = detalleOrdenId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Long getValidadoPorId() { return validadoPorId; }
    public void setValidadoPorId(Long validadoPorId) { this.validadoPorId = validadoPorId; }
}