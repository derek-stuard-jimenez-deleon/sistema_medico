package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.SucursalRequest;
import com.sistemamedico.app.service.SucursalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/catalogos/sucursales")
public class SucursalMvcController {

    private final SucursalService sucursalService;

    public SucursalMvcController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("sucursales", sucursalService.listarTodos());
        return "sucursales-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        SucursalRequest req = new SucursalRequest();
        req.setActivo(true);
        model.addAttribute("sucursalRequest", req);
        return "sucursales-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("sucursalRequest") SucursalRequest req, RedirectAttributes redirectAttributes) {
        try {
            sucursalService.crear(req);
            redirectAttributes.addFlashAttribute("mensajeExito", "Sucursal creada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogos/sucursales/nuevo";
        }
        return "redirect:/catalogos/sucursales";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var sucursal = sucursalService.buscarPorId(id);
        SucursalRequest req = new SucursalRequest();
        req.setNombre(sucursal.getNombre());
        req.setDireccion(sucursal.getDireccion());
        req.setTelefono(sucursal.getTelefono());
        req.setHorarioAtencion(sucursal.getHorarioAtencion());
        req.setActivo(sucursal.isActivo());
        
        model.addAttribute("sucursalRequest", req);
        model.addAttribute("editMode", true);
        model.addAttribute("sucursalId", id);
        return "sucursales-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("sucursalRequest") SucursalRequest req, RedirectAttributes redirectAttributes) {
        try {
            sucursalService.actualizar(id, req);
            redirectAttributes.addFlashAttribute("mensajeExito", "Sucursal actualizada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogos/sucursales/editar/" + id;
        }
        return "redirect:/catalogos/sucursales";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sucursalService.eliminar(id, 1L);
            redirectAttributes.addFlashAttribute("mensajeExito", "Sucursal eliminada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/catalogos/sucursales";
    }
}
