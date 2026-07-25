package com.sistemamedico.app.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PagoRequest {

    @NotNull(message = "Debe indicar el tipo de origen del pago.")
    private String tipoOrigen; // CITA, LABORATORIO, FARMACIA

    @NotNull(message = "Debe indicar la referencia (id de la cita, orden o despacho).")
    private Long referenciaId;

    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero.")
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio.")
    private String metodoPago; // EFECTIVO, VISA_CREDITO, MASTERCARD, DEBITO

    private BigDecimal montoRecibido; // solo si es efectivo

    private String ultimosDigitosTarjeta; // solo si es tarjeta

    private Long cajeroId; // null si es pago en linea

    @NotNull(message = "Debe indicar la sucursal.")
    private Long sucursalId;

    // --- Getters y setters ---
    public String getTipoOrigen() { return tipoOrigen; }
    public void setTipoOrigen(String tipoOrigen) { this.tipoOrigen = tipoOrigen; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public BigDecimal getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(BigDecimal montoRecibido) { this.montoRecibido = montoRecibido; }
    public String getUltimosDigitosTarjeta() { return ultimosDigitosTarjeta; }
    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) { this.ultimosDigitosTarjeta = ultimosDigitosTarjeta; }
    public Long getCajeroId() { return cajeroId; }
    public void setCajeroId(Long cajeroId) { this.cajeroId = cajeroId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
}