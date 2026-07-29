package com.sistemamedico.app.dto;

import java.util.List;

public class DespachoMedicamentoResponse {

    private Long id;
    private String pacienteNombre;
    private String farmaceuticoNombre;
    private String estado;
    private List<DetalleDespachoResponse> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getFarmaceuticoNombre() { return farmaceuticoNombre; }
    public void setFarmaceuticoNombre(String farmaceuticoNombre) { this.farmaceuticoNombre = farmaceuticoNombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetalleDespachoResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleDespachoResponse> detalles) { this.detalles = detalles; }
}