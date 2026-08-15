package com.sistemamedico.app.service;

import com.sistemamedico.app.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthenticationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);
    private final UsuarioRepository usuarioRepository;
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_TIME_MINUTES = 15;

    @Autowired
    public AuthenticationEventListener(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        logger.warn("Fallo de autenticación para el usuario: {}", username); // <-- MENSAJE DE LOG

        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            if (usuario.isActivo() && (usuario.getBloqueadoHasta() == null || usuario.getBloqueadoHasta().isBefore(LocalDateTime.now()))) {
                usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                logger.info("Incrementando intentos fallidos para {}. Nuevo total: {}", username, usuario.getIntentosFallidos());

                if (usuario.getIntentosFallidos() >= MAX_ATTEMPTS) {
                    usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(LOCK_TIME_MINUTES));
                    logger.warn("Usuario {} bloqueado hasta las {}", username, usuario.getBloqueadoHasta());
                }
                usuarioRepository.save(usuario);
            }
        });
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        logger.info("Autenticación exitosa para el usuario: {}", username);
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            if (usuario.getIntentosFallidos() > 0 || usuario.getBloqueadoHasta() != null) {
                usuario.setIntentosFallidos(0);
                usuario.setBloqueadoHasta(null);
                usuarioRepository.save(usuario);
                logger.info("Reinicio de intentos fallidos para el usuario: {}", username);
            }
        });
    }
}