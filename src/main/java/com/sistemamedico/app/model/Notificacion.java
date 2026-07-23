package com.sistemamedico.app.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import com.sistemamedico.app.model.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@SQLRestriction("eliminado = false")
public class Notificacion extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String tipo; // ej: "Confirmacion Cita", "Recordatorio", "Comprobante Pago"

    @Column(nullable = false, length = 100)
    private String destinatario; // correo electronico

    @Column(nullable = false, length = 200)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cuerpo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoNotificacion estado = EstadoNotificacion.PENDIENTE;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "intentos_envio", nullable = false)
    private int intentosEnvio = 0;

    public enum EstadoNotificacion {
        PENDIENTE, ENVIADO, FALLIDO, REINTENTANDO
    }

    // --- Getters y setters ---
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
    public EstadoNotificacion getEstado() { return estado; }
    public void setEstado(EstadoNotificacion estado) { this.estado = estado; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public int getIntentosEnvio() { return intentosEnvio; }
    public void setIntentosEnvio(int intentosEnvio) { this.intentosEnvio = intentosEnvio; }
}