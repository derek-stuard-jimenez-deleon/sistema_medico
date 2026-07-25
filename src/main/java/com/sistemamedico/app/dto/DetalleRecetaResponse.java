package com.sistemamedico.app.dto;

public class DetalleRecetaResponse {

    private Long id;
    private String medicamentoNombre;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicacionesEspeciales;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicamentoNombre() { return medicamentoNombre; }
    public void setMedicamentoNombre(String medicamentoNombre) { this.medicamentoNombre = medicamentoNombre; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public String getIndicacionesEspeciales() { return indicacionesEspeciales; }
    public void setIndicacionesEspeciales(String indicacionesEspeciales) { this.indicacionesEspeciales = indicacionesEspeciales; }
}