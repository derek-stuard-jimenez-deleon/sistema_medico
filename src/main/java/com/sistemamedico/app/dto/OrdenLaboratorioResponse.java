package com.sistemamedico.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenLaboratorioResponse {

    private Long id;
    private String pacienteNombre;
    private String pacienteDpi;
    private String medicoNombre;
    private String estado;
    private BigDecimal montoTotal;
    private LocalDateTime fechaCreacion;
    private List<DetalleOrdenLaboratorioResponse> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getPacienteDpi() { return pacienteDpi; }
    public void setPacienteDpi(String pacienteDpi) { this.pacienteDpi = pacienteDpi; }
    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public List<DetalleOrdenLaboratorioResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenLaboratorioResponse> detalles) { this.detalles = detalles; }
}
