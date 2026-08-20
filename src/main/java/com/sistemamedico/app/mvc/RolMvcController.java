package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.RolRequest;
import com.sistemamedico.app.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/catalogos/roles")
public class RolMvcController {

    private final RolService rolService;

    public RolMvcController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        return "roles-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        RolRequest req = new RolRequest();
        req.setActivo(true);
        model.addAttribute("rolRequest", req);
        return "roles-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("rolRequest") RolRequest req, RedirectAttributes redirectAttributes) {
        try {
            rolService.crear(req);
            redirectAttributes.addFlashAttribute("mensajeExito", "Rol creado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogos/roles/nuevo";
        }
        return "redirect:/catalogos/roles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var rol = rolService.buscarPorId(id);
        RolRequest req = new RolRequest();
        req.setNombre(rol.getNombre());
        req.setDescripcion(rol.getDescripcion());
        req.setActivo(rol.isActivo());
        
        model.addAttribute("rolRequest", req);
        model.addAttribute("editMode", true);
        model.addAttribute("rolId", id);
        return "roles-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("rolRequest") RolRequest req, RedirectAttributes redirectAttributes) {
        try {
            rolService.actualizar(id, req);
            redirectAttributes.addFlashAttribute("mensajeExito", "Rol actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogos/roles/editar/" + id;
        }
        return "redirect:/catalogos/roles";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.eliminar(id, 1L);
            redirectAttributes.addFlashAttribute("mensajeExito", "Rol eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/catalogos/roles";
    }
}
