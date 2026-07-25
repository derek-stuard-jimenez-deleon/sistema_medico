package com.sistemamedico.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConsultaMedicaRequest {

    @NotNull(message = "Debe indicar la cita.")
    private Long citaId;

    @NotNull(message = "Debe indicar el médico.")
    private Long medicoId;

    @NotBlank(message = "El motivo de consulta es obligatorio.")
    @Size(max = 2000, message = "El motivo no puede exceder 2000 caracteres.")
    private String motivoConsulta;

    @NotBlank(message = "Los hallazgos clínicos son obligatorios.")
    @Size(max = 5000, message = "Los hallazgos no pueden exceder 5000 caracteres.")
    private String hallazgosClinicos;

    @Size(max = 5000, message = "El diagnóstico no puede exceder 5000 caracteres.")
    private String diagnostico; // obligatorio solo al cerrar la consulta

    @Size(max = 10, message = "El código CIE-10 no puede exceder 10 caracteres.")
    private String codigoCie10;

    @NotBlank(message = "El plan de tratamiento es obligatorio.")
    @Size(max = 5000, message = "El plan no puede exceder 5000 caracteres.")
    private String planTratamiento;

    private boolean cerrar = false; // true para cerrar la consulta (requiere diagnostico)

    // --- Getters y setters ---
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
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
    public boolean isCerrar() { return cerrar; }
    public void setCerrar(boolean cerrar) { this.cerrar = cerrar; }
}