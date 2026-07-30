package com.sistemamedico.app.service;

import com.sistemamedico.app.model.Notificacion;
import com.sistemamedico.app.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final JavaMailSender mailSender;

    @Value("${app.notificaciones.remitente}")
    private String remitente;

    public NotificacionService(NotificacionRepository notificacionRepository, JavaMailSender mailSender) {
        this.notificacionRepository = notificacionRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void enviar(String tipo, String destinatario, String asunto, String cuerpo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(tipo);
        notificacion.setDestinatario(destinatario);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(cuerpo);
        notificacion.setEstado(Notificacion.EstadoNotificacion.PENDIENTE);
        notificacion.setIntentosEnvio(0);
        notificacion = notificacionRepository.save(notificacion);

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mailSender.send(mensaje);

            notificacion.setEstado(Notificacion.EstadoNotificacion.ENVIADO);
            notificacion.setFechaEnvio(LocalDateTime.now());
        } catch (Exception e) {
            notificacion.setEstado(Notificacion.EstadoNotificacion.FALLIDO);
            notificacion.setIntentosEnvio(notificacion.getIntentosEnvio() + 1);
            // No relanzamos la excepcion: un correo fallido no debe tumbar
            // la operacion principal (crear cita, registrar pago, etc.)
        }

        notificacionRepository.save(notificacion);
    }
}