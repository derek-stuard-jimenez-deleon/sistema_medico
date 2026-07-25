package com.sistemamedico.app.dto;

import java.util.List;

public class RecetaMedicaResponse {

    private Long id;
    private Long consultaId;
    private String pacienteNombre;
    private String medicoNombre;
    private List<DetalleRecetaResponse> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
    public List<DetalleRecetaResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecetaResponse> detalles) { this.detalles = detalles; }
}