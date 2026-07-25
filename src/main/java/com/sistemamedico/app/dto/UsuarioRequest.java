package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

public class UsuarioRequest {

    @NotBlank(message = "El campo Usuario es obligatorio.")
    @Size(min = 8, max = 9, message = "El usuario debe tener entre 8 y 9 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El usuario debe contener únicamente caracteres alfanuméricos.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 12, message = "La contraseña debe contener al menos 12 caracteres.")
    private String password;

    @NotBlank(message = "El campo Nombre es obligatorio.")
    @Size(min = 10, max = 100, message = "El nombre debe contener entre 10 y 100 caracteres.")
    private String nombreCompleto;

    @Pattern(regexp = "^\\d{13}$", message = "El DPI debe contener exactamente 13 dígitos.")
    private String dpi; // opcional

    @Size(min = 8, max = 9, message = "El NIT debe contener entre 8 y 9 caracteres.")
    private String nit; // opcional

    @Pattern(regexp = "^\\d{8}$", message = "El teléfono debe contener exactamente 8 dígitos.")
    private String telefono; // opcional

    @Size(min = 5, max = 50, message = "El número de seguro debe contener entre 5 y 50 caracteres.")
    private String numeroSeguro; // opcional

    @NotNull(message = "Debe seleccionar un rol para el usuario.")
    private Long rolId;

    @NotNull(message = "Debe seleccionar una sucursal para el usuario.")
    private Long sucursalId;

    private Long especialidadId; // obligatorio solo si el rol es Medico, se valida en el Service

    private boolean activo = true;

    // --- Getters y setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNumeroSeguro() { return numeroSeguro; }
    public void setNumeroSeguro(String numeroSeguro) { this.numeroSeguro = numeroSeguro; }
    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
    public Long getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}