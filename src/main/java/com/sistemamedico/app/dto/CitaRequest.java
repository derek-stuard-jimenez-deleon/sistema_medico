package com.sistemamedico.app.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CitaRequest {

    @NotNull(message = "Debe indicar el paciente.")
    private Long pacienteId;

    @NotNull(message = "Debe indicar el médico.")
    private Long medicoId;

    @NotNull(message = "Debe indicar la especialidad.")
    private Long especialidadId;

    @NotNull(message = "Debe indicar la sucursal.")
    private Long sucursalId;

    @NotNull(message = "Debe indicar la fecha y hora de la cita.")
    @Future(message = "La fecha de la cita debe ser futura.")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo de la visita es obligatorio.")
    @Size(max = 2000, message = "El motivo no puede exceder 2000 caracteres.")
    private String motivoVisita;

    // --- Getters y setters ---
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public Long getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivoVisita() { return motivoVisita; }
    public void setMotivoVisita(String motivoVisita) { this.motivoVisita = motivoVisita; }
}