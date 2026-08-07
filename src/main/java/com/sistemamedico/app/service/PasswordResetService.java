package com.sistemamedico.app.service;

import com.sistemamedico.app.model.Paciente;
import com.sistemamedico.app.model.PasswordResetToken;
import com.sistemamedico.app.repository.PacienteRepository;
import com.sistemamedico.app.repository.PasswordResetTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PacienteRepository pacienteRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final NotificacionService notificacionService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PacienteRepository pacienteRepository,
                                PasswordResetTokenRepository tokenRepository,
                                NotificacionService notificacionService,
                                PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.tokenRepository = tokenRepository;
        this.notificacionService = notificacionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void solicitarRecuperacion(String usernameOCorreo) {
        // Busca por username, y si no, por correo
        Paciente paciente = pacienteRepository.findByUsername(usernameOCorreo)
                .orElse(pacienteRepository.findAll().stream()
                        .filter(p -> p.getCorreo().equalsIgnoreCase(usernameOCorreo))
                        .findFirst()
                        .orElse(null));

        // Por seguridad, si no existe la cuenta, no revelamos nada -- simplemente no hacemos nada.
        if (paciente == null) {
            return;
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setPacienteId(paciente.getId());
        token.setFechaExpiracion(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(token);

        String enlace = "http://localhost:8080/reset-password?token=" + token.getToken();

        notificacionService.enviar(
                "Recuperacion de contrasena",
                paciente.getCorreo(),
                "Recupera tu contraseña - Sistema Medico",
                "Hola " + paciente.getNombreCompleto() + ",\n\n" +
                        "Recibimos una solicitud para restablecer tu contraseña.\n" +
                        "Da clic en el siguiente enlace (valido por 30 minutos):\n\n" +
                        enlace + "\n\n" +
                        "Si no solicitaste esto, puedes ignorar este correo.\n\n" +
                        "Sistema Medico 2026"
        );
    }

    public boolean tokenValido(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isUsado() && t.getFechaExpiracion().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Transactional
    public void restablecerContrasena(String token, String nuevaContrasena) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("El enlace no es válido."));

        if (resetToken.isUsado() || resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El enlace ha expirado o ya fue utilizado.");
        }

        Paciente paciente = pacienteRepository.findById(resetToken.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado."));

        paciente.setPasswordHash(passwordEncoder.encode(nuevaContrasena));
        pacienteRepository.save(paciente);

        resetToken.setUsado(true);
        tokenRepository.save(resetToken);
    }
}