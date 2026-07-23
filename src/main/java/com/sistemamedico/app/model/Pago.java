package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@SQLRestriction("eliminado = false")
public class Pago extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_origen", nullable = false, length = 20)
    private TipoOrigenPago tipoOrigen;

    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId; // id de Cita, OrdenLaboratorio o DespachoMedicamento segun tipoOrigen

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @Column(name = "monto_recibido", precision = 10, scale = 2)
    private BigDecimal montoRecibido; // solo efectivo

    @Column(precision = 10, scale = 2)
    private BigDecimal cambio; // solo efectivo

    @Column(name = "ultimos_digitos_tarjeta", length = 4)
    private String ultimosDigitosTarjeta; // solo tarjeta, RNF-012

    @Column(name = "numero_transaccion", nullable = false, unique = true, length = 50)
    private String numeroTransaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cajero_id")
    private Usuario cajero; // null en pagos en linea (CU-04)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    public enum TipoOrigenPago {
        CITA, LABORATORIO, FARMACIA
    }

    public enum MetodoPago {
        EFECTIVO, VISA_CREDITO, MASTERCARD, DEBITO
    }

    // --- Getters y setters ---
    public TipoOrigenPago getTipoOrigen() { return tipoOrigen; }
    public void setTipoOrigen(TipoOrigenPago tipoOrigen) { this.tipoOrigen = tipoOrigen; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public BigDecimal getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(BigDecimal montoRecibido) { this.montoRecibido = montoRecibido; }
    public BigDecimal getCambio() { return cambio; }
    public void setCambio(BigDecimal cambio) { this.cambio = cambio; }
    public String getUltimosDigitosTarjeta() { return ultimosDigitosTarjeta; }
    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) { this.ultimosDigitosTarjeta = ultimosDigitosTarjeta; }
    public String getNumeroTransaccion() { return numeroTransaccion; }
    public void setNumeroTransaccion(String numeroTransaccion) { this.numeroTransaccion = numeroTransaccion; }
    public Usuario getCajero() { return cajero; }
    public void setCajero(Usuario cajero) { this.cajero = cajero; }
    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}