package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

public class PacienteRequest {

    @NotBlank(message = "El DPI es obligatorio.")
    @Pattern(regexp = "^\\d{13}$", message = "El DPI debe contener exactamente 13 dígitos.")
    private String dpi;

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(min = 10, max = 100, message = "El nombre debe contener entre 10 y 100 caracteres.")
    private String nombreCompleto;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "^\\d{8}$", message = "El teléfono debe contener exactamente 8 dígitos.")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El formato del correo no es válido.")
    private String correo;

    @Pattern(regexp = "^$|^.{5,50}$", message = "El número de seguro debe contener entre 5 y 50 caracteres.")
    private String numeroSeguro; // opcional — acepta vacío o 5-50 caracteres

    @NotBlank(message = "El campo Usuario es obligatorio.")
    @Size(min = 8, max = 9, message = "El usuario debe tener entre 8 y 9 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El usuario debe contener únicamente caracteres alfanuméricos.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 12, message = "La contraseña debe contener al menos 12 caracteres.")
    private String password;

    private boolean activo = true;

    // --- Getters y setters ---
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
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}