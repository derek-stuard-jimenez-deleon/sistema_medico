package com.sistemamedico.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RecetaMedicaRequest {

    @NotNull(message = "Debe indicar la consulta médica.")
    private Long consultaId;

    @NotEmpty(message = "La receta debe contener al menos un medicamento.")
    @Valid
    private List<DetalleRecetaRequest> detalles;

    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
    public List<DetalleRecetaRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecetaRequest> detalles) { this.detalles = detalles; }
}