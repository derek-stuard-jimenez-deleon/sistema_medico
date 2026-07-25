package com.sistemamedico.app.dto;

public class ConsultaMedicaResponse {

    private Long id;
    private Long citaId;
    private String pacienteNombre;
    private String medicoNombre;
    private String motivoConsulta;
    private String hallazgosClinicos;
    private String diagnostico;
    private String codigoCie10;
    private String planTratamiento;
    private String estado;

    // --- Getters y setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    public String getHallazgosClinicos() { return hallazgosClinicos; }
    public void setHallazgosClinicos(String hallazgosClinicos) { this.hallazgosClinicos = hallazgosClinicos; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getCodigoCie10() { return codigoCie10; }
    public void setCodigoCie10(String codigoCie10) { this.codigoCie10 = codigoCie10; }
    public String getPlanTratamiento() { return planTratamiento; }
    public void setPlanTratamiento(String planTratamiento) { this.planTratamiento = planTratamiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}