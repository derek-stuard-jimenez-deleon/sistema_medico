package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotNull;

public class SedeEspecialidadRequest {

    @NotNull(message = "Debe indicar la sucursal.")
    private Long sucursalId;

    @NotNull(message = "Debe indicar la especialidad.")
    private Long especialidadId;

    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
    public Long getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; }
}