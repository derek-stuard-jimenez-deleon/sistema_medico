package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.PacienteRequest;
import com.sistemamedico.app.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final PacienteService pacienteService;

    public RegistroController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(@RequestParam(required = false) String dpi, Model model) {
        if (!model.containsAttribute("pacienteRequest")) {
            PacienteRequest pacienteRequest = new PacienteRequest();
            if (dpi != null) {
                pacienteRequest.setDpi(dpi);
            }
            model.addAttribute("pacienteRequest", pacienteRequest);
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("pacienteRequest") PacienteRequest pacienteRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   org.springframework.security.core.Authentication authentication) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pacienteRequest", bindingResult);
            redirectAttributes.addFlashAttribute("pacienteRequest", pacienteRequest);
            return "redirect:/registro";
        }

        try {
            pacienteService.crearPaciente(pacienteRequest);
            
            // Si es un empleado (Recepcionista) quien lo registró, lo devolvemos a Recepción
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Paciente " + pacienteRequest.getNombreCompleto() + " registrado exitosamente.");
                return "redirect:/recepcion?term=" + pacienteRequest.getDpi() + "&tipoBusqueda=dpi";
            }
            
            // Si es el paciente registrándose a sí mismo desde fuera, va al login
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Registro exitoso! Ahora puedes iniciar sesión.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorRegistro", e.getMessage());
            redirectAttributes.addFlashAttribute("pacienteRequest", pacienteRequest);
            return "redirect:/registro";
        }
    }
}