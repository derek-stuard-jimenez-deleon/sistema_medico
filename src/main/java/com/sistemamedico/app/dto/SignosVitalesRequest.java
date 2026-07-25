package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class SignosVitalesRequest {

    @NotNull(message = "Debe indicar la cita.")
    private Long citaId;

    @NotNull(message = "Debe indicar el enfermero.")
    private Long enfermeroId;

    @NotNull(message = "La presión sistólica es obligatoria.")
    @Min(value = 60, message = "La presión sistólica debe ser al menos 60 mmHg.")
    @Max(value = 250, message = "La presión sistólica no puede exceder 250 mmHg.")
    private Integer presionSistolica;

    @NotNull(message = "La presión diastólica es obligatoria.")
    @Min(value = 40, message = "La presión diastólica debe ser al menos 40 mmHg.")
    @Max(value = 150, message = "La presión diastólica no puede exceder 150 mmHg.")
    private Integer presionDiastolica;

    @NotNull(message = "La temperatura es obligatoria.")
    @DecimalMin(value = "34.0", message = "La temperatura debe ser al menos 34.0 C.")
    @DecimalMax(value = "42.0", message = "La temperatura no puede exceder 42.0 C.")
    private BigDecimal temperatura;

    @NotNull(message = "El peso es obligatorio.")
    @DecimalMin(value = "0.5", message = "El peso debe ser al menos 0.5 kg.")
    @DecimalMax(value = "300", message = "El peso no puede exceder 300 kg.")
    private BigDecimal peso;

    @NotNull(message = "La talla es obligatoria.")
    @DecimalMin(value = "30", message = "La talla debe ser al menos 30 cm.")
    @DecimalMax(value = "250", message = "La talla no puede exceder 250 cm.")
    private BigDecimal talla;

    @NotNull(message = "La frecuencia cardiaca es obligatoria.")
    @Min(value = 30, message = "La frecuencia cardiaca debe ser al menos 30 lpm.")
    @Max(value = 220, message = "La frecuencia cardiaca no puede exceder 220 lpm.")
    private Integer frecuenciaCardiaca;

    // --- Getters y setters ---
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public Long getEnfermeroId() { return enfermeroId; }
    public void setEnfermeroId(Long enfermeroId) { this.enfermeroId = enfermeroId; }
    public Integer getPresionSistolica() { return presionSistolica; }
    public void setPresionSistolica(Integer presionSistolica) { this.presionSistolica = presionSistolica; }
    public Integer getPresionDiastolica() { return presionDiastolica; }
    public void setPresionDiastolica(Integer presionDiastolica) { this.presionDiastolica = presionDiastolica; }
    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getTalla() { return talla; }
    public void setTalla(BigDecimal talla) { this.talla = talla; }
    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
}