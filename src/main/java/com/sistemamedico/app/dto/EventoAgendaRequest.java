package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class EventoAgendaRequest {

    @NotNull(message = "Debe indicar el médico.")
    private Long medicoId;

    @NotBlank(message = "El título es obligatorio.")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres.")
    private String titulo;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria.")
    private LocalDateTime fechaFin;

    @NotBlank(message = "El tipo de evento es obligatorio.")
    private String tipoEvento; // BLOQUEO_DISPONIBILIDAD, EVENTO_PERSONAL, CAPACITACION, VACACIONES

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres.")
    private String descripcion;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "El color debe ser un código hexadecimal válido, ej. #FF5733.")
    private String color;

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}