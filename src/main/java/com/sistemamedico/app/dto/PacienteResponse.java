package com.sistemamedico.app.dto;

public class PacienteResponse {

    private Long id;
    private String dpi;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String numeroSeguro;
    private String username;
    private boolean activo;

    // --- Getters y setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getNumeroSeguro() { return numeroSeguro; }
    public void setNumeroSeguro(String numeroSeguro) { this.numeroSeguro = numeroSeguro; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}