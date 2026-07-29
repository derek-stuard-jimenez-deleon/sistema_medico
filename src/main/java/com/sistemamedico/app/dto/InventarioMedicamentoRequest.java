package com.sistemamedico.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventarioMedicamentoRequest {

    @NotNull(message = "Debe indicar el medicamento.")
    private Long medicamentoId;

    @NotNull(message = "Debe indicar la sucursal.")
    private Long sucursalId;

    @NotNull(message = "El stock actual es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stockActual;

    @NotNull(message = "El stock mínimo es obligatorio.")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo.")
    private Integer stockMinimo;

    public Long getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
}