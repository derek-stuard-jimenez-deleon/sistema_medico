package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.PacienteRequest;
import com.sistemamedico.app.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PacienteMvcController {

    private final PacienteService pacienteService;

    public PacienteMvcController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/pacientes")
    public String listar(Model model) {
        var pagina = pacienteService.listarTodos(PageRequest.of(0, 20));
        model.addAttribute("pacientes", pagina.getContent());
        return "pacientes";
    }

    // Muestra el formulario vacio
    @GetMapping("/pacientes/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("pacienteRequest", new PacienteRequest());
        return "paciente-form"; // busca templates/paciente-form.html
    }

    // Procesa el envio del formulario
    @PostMapping("/pacientes")
    public String crear(@Valid @ModelAttribute("pacienteRequest") PacienteRequest pacienteRequest,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "paciente-form"; // vuelve al formulario mostrando los errores
        }

        try {
            pacienteService.crear(pacienteRequest);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorNegocio", e.getMessage());
            return "paciente-form";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Paciente registrado exitosamente.");
        return "redirect:/pacientes";
    }
}