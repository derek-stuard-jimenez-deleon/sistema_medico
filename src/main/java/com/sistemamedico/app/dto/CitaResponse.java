package com.sistemamedico.app.dto;

import java.time.LocalDateTime;

public class CitaResponse {

    private Long id;
    private String pacienteNombre;
    private String pacienteDpi;
    private Long medicoId; // Añadido
    private String medicoNombre;
    private Long especialidadId; // Añadido
    private String especialidadNombre;
    private Long sucursalId; // Añadido
    private String sucursalNombre;
    private LocalDateTime fechaHora;
    private String motivoVisita;
    private String estado;
    private String tipo;
    private boolean verificada;

    // --- Getters y setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getPacienteDpi() { return pacienteDpi; }
    public void setPacienteDpi(String pacienteDpi) { this.pacienteDpi = pacienteDpi; }
    public Long getMedicoId() { return medicoId; } // Getter para medicoId
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; } // Setter para medicoId
    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
    public Long getEspecialidadId() { return especialidadId; } // Getter para especialidadId
    public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; } // Setter para especialidadId
    public String getEspecialidadNombre() { return especialidadNombre; }
    public void setEspecialidadNombre(String especialidadNombre) { this.especialidadNombre = especialidadNombre; }
    public Long getSucursalId() { return sucursalId; } // Getter para sucursalId
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; } // Setter para sucursalId
    public String getSucursalNombre() { return sucursalNombre; }
    public void setSucursalNombre(String sucursalNombre) { this.sucursalNombre = sucursalNombre; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivoVisita() { return motivoVisita; }
    public void setMotivoVisita(String motivoVisita) { this.motivoVisita = motivoVisita; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isVerificada() { return verificada; }
    public void setVerificada(boolean verificada) { this.verificada = verificada; }
}