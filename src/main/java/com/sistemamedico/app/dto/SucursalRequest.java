package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SucursalRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres.")
    private String nombre;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres.")
    private String direccion;

    @Pattern(regexp = "^\\d{8}$", message = "El teléfono debe contener exactamente 8 dígitos.")
    private String telefono;

    @Size(max = 100, message = "El horario no puede exceder 100 caracteres.")
    private String horarioAtencion;

    private boolean activo = true;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getHorarioAtencion() { return horarioAtencion; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}