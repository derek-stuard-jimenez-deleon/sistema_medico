package com.sistemamedico.app.dto;

public class DetalleOrdenLaboratorioResponse {

    private Long id;
    private String examenNombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExamenNombre() { return examenNombre; }
    public void setExamenNombre(String examenNombre) { this.examenNombre = examenNombre; }
}