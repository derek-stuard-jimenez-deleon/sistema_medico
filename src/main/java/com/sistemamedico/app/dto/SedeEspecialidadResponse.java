package com.sistemamedico.app.dto;

public class SedeEspecialidadResponse {

    private Long id;
    private String sucursalNombre;
    private Long especialidadId;
    private String especialidadNombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSucursalNombre() { return sucursalNombre; }
    public void setSucursalNombre(String sucursalNombre) { this.sucursalNombre = sucursalNombre; }
    public Long getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(Long especialidadId) { this.especialidadId = especialidadId; }
    public String getEspecialidadNombre() { return especialidadNombre; }
    public void setEspecialidadNombre(String especialidadNombre) { this.especialidadNombre = especialidadNombre; }
}