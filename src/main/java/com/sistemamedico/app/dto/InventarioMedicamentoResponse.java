package com.sistemamedico.app.dto;

public class InventarioMedicamentoResponse {

    private Long id;
    private String medicamentoNombre;
    private String sucursalNombre;
    private Integer stockActual;
    private Integer stockMinimo;
    private boolean stockBajo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicamentoNombre() { return medicamentoNombre; }
    public void setMedicamentoNombre(String medicamentoNombre) { this.medicamentoNombre = medicamentoNombre; }
    public String getSucursalNombre() { return sucursalNombre; }
    public void setSucursalNombre(String sucursalNombre) { this.sucursalNombre = sucursalNombre; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public boolean isStockBajo() { return stockBajo; }
    public void setStockBajo(boolean stockBajo) { this.stockBajo = stockBajo; }
}