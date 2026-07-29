package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotNull;

public class DespachoMedicamentoRequest {

    @NotNull(message = "Debe indicar la receta.")
    private Long recetaId;

    @NotNull(message = "Debe indicar el farmacéutico.")
    private Long farmaceuticoId;

    @NotNull(message = "Debe indicar la sucursal desde donde se despacha.")
    private Long sucursalId;

    public Long getRecetaId() { return recetaId; }
    public void setRecetaId(Long recetaId) { this.recetaId = recetaId; }
    public Long getFarmaceuticoId() { return farmaceuticoId; }
    public void setFarmaceuticoId(Long farmaceuticoId) { this.farmaceuticoId = farmaceuticoId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
}