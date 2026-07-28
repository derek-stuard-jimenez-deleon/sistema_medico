package com.sistemamedico.app.dto;

import java.math.BigDecimal;

public class ResultadoLaboratorioResponse {

    private Long id;
    private String examenNombre;
    private BigDecimal valor;
    private String unidadMedida;
    private boolean fueraDeRango;
    private String validadoPorNombre;
    private boolean publicado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExamenNombre() { return examenNombre; }
    public void setExamenNombre(String examenNombre) { this.examenNombre = examenNombre; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public boolean isFueraDeRango() { return fueraDeRango; }
    public void setFueraDeRango(boolean fueraDeRango) { this.fueraDeRango = fueraDeRango; }
    public String getValidadoPorNombre() { return validadoPorNombre; }
    public void setValidadoPorNombre(String validadoPorNombre) { this.validadoPorNombre = validadoPorNombre; }
    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }
}