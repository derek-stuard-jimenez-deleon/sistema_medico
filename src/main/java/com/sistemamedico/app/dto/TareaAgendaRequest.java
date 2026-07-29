package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TareaAgendaRequest {

    @NotNull(message = "Debe indicar el médico.")
    private Long medicoId;

    @NotBlank(message = "El título es obligatorio.")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres.")
    private String titulo;

    @NotBlank(message = "La prioridad es obligatoria.")
    private String prioridad; // ALTA, MEDIA, BAJA

    private LocalDate fechaLimite; // opcional

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
}