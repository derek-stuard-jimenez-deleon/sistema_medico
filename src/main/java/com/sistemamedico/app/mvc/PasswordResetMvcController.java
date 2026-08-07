package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetMvcController {

    private final PasswordResetService passwordResetService;

    public PasswordResetMvcController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password")
    public String mostrarFormularioSolicitud() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String procesarSolicitud(@RequestParam String usernameOCorreo, Model model) {
        passwordResetService.solicitarRecuperacion(usernameOCorreo);
        model.addAttribute("mensaje",
                "Si la cuenta existe, te enviamos un correo con instrucciones para recuperar tu contraseña.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String mostrarFormularioReset(@RequestParam String token, Model model) {
        if (!passwordResetService.tokenValido(token)) {
            model.addAttribute("error", "El enlace no es válido o ha expirado. Solicita uno nuevo.");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String procesarReset(@RequestParam String token,
                                @RequestParam String password,
                                @RequestParam String confirmarPassword,
                                Model model) {
        if (!password.equals(confirmarPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "reset-password";
        }
        if (password.length() < 12) {
            model.addAttribute("token", token);
            model.addAttribute("error", "La contraseña debe contener al menos 12 caracteres.");
            return "reset-password";
        }

        try {
            passwordResetService.restablecerContrasena(token, password);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "reset-password";
        }

        model.addAttribute("exito", true);
        return "reset-password";
    }
}