package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.EspecialidadRequest;
import com.sistemamedico.app.dto.EspecialidadResponse;
import com.sistemamedico.app.service.EspecialidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/catalogos/especialidades")
public class EspecialidadMvcController {

    private final EspecialidadService especialidadService;

    public EspecialidadMvcController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "especialidades-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        EspecialidadRequest req = new EspecialidadRequest();
        req.setActivo(true);
        model.addAttribute("especialidadRequest", req);
        return "especialidades-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("especialidadRequest") EspecialidadRequest req, 
                          @RequestParam(required = false) Long id, 
                          RedirectAttributes redirectAttributes) {
        try {
            if (id == null) {
                especialidadService.crear(req);
                redirectAttributes.addFlashAttribute("mensajeExito", "Especialidad creada con ?xito.");
            } else {
                especialidadService.actualizar(id, req);
                redirectAttributes.addFlashAttribute("mensajeExito", "Especialidad actualizada con ?xito.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogos/especialidades/nuevo";
        }
        return "redirect:/catalogos/especialidades";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // En la vida real sacamos el usuario logueado. Aqui mockeamos admin = 1.
        especialidadService.eliminar(id, 1L);
        redirectAttributes.addFlashAttribute("mensajeExito", "Especialidad dada de baja con ?xito.");
        return "redirect:/catalogos/especialidades";
    }
}
