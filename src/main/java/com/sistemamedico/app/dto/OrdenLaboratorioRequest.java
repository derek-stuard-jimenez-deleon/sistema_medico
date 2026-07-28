package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrdenLaboratorioRequest {

    @NotNull(message = "Debe indicar la consulta médica.")
    private Long consultaId;

    @NotEmpty(message = "Debe seleccionar al menos un examen.")
    private List<Long> examenesIds;

    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
    public List<Long> getExamenesIds() { return examenesIds; }
    public void setExamenesIds(List<Long> examenesIds) { this.examenesIds = examenesIds; }
}