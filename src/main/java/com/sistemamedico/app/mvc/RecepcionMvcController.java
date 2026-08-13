package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecepcionMvcController {

    private final CitaService citaService;

    public RecepcionMvcController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping("/recepcion")
    public String buscar(@RequestParam(required = false) String dpi, Model model) {
        if (dpi != null && !dpi.isBlank()) {
            model.addAttribute("citas", citaService.buscarPorPaciente(dpi));
            model.addAttribute("dpiBuscado", dpi);
        }
        return "recepcion";
    }

    @PostMapping("/recepcion/{id}/verificar")
    public String verificar(@PathVariable Long id, @RequestParam String dpi, RedirectAttributes redirectAttributes) {
        try {
            citaService.marcarVerificada(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Llegada del paciente verificada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        return "redirect:/recepcion?dpi=" + dpi;
    }
}