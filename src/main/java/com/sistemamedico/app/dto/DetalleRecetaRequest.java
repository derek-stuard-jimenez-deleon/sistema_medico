package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DetalleRecetaRequest {

    @NotNull(message = "Debe indicar el medicamento.")
    private Long medicamentoId;

    @NotBlank(message = "La dosis es obligatoria.")
    @Size(max = 200, message = "La dosis no puede exceder 200 caracteres.")
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria.")
    @Size(max = 200, message = "La frecuencia no puede exceder 200 caracteres.")
    private String frecuencia;

    @NotBlank(message = "La duración es obligatoria.")
    @Size(max = 100, message = "La duración no puede exceder 100 caracteres.")
    private String duracion;

    @Size(max = 1000, message = "Las indicaciones no pueden exceder 1000 caracteres.")
    private String indicacionesEspeciales;

    public Long getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public String getIndicacionesEspeciales() { return indicacionesEspeciales; }
    public void setIndicacionesEspeciales(String indicacionesEspeciales) { this.indicacionesEspeciales = indicacionesEspeciales; }
}