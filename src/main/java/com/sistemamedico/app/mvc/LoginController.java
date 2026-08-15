package com.sistemamedico.app.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            Exception exception = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null) {
                errorMessage = exception.getMessage();
                // Limpiamos la excepción de la sesión para que no reaparezca al refrescar
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
}