package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.UsuarioRequest;
import com.sistemamedico.app.service.EspecialidadService;
import com.sistemamedico.app.service.RolService;
import com.sistemamedico.app.service.SucursalService;
import com.sistemamedico.app.service.UsuarioService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioMvcController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final SucursalService sucursalService;
    private final EspecialidadService especialidadService;

    public UsuarioMvcController(UsuarioService usuarioService, RolService rolService, SucursalService sucursalService, EspecialidadService especialidadService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.sucursalService = sucursalService;
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public String listar(Model model, Principal principal) {
        var current = usuarioService.buscarPorUsername(principal.getName());
        boolean isAdminSede = "ADMIN_SEDE".equals(current.getRolNombre());

        List<?> usuarios = isAdminSede 
            ? usuarioService.listarParaSede(current.getSucursalId(), PageRequest.of(0, 100)).getContent()
            : usuarioService.listarTodos(PageRequest.of(0, 100)).getContent();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("isAdminSede", isAdminSede);
        return "usuarios-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model, Principal principal) {
        var current = usuarioService.buscarPorUsername(principal.getName());
        boolean isAdminSede = "ADMIN_SEDE".equals(current.getRolNombre());

        UsuarioRequest req = new UsuarioRequest();
        req.setActivo(true);
        req.setPassword("password123");
        
        if (isAdminSede) {
            req.setSucursalId(current.getSucursalId()); // Force own branch
        }

        model.addAttribute("usuarioRequest", req);
        model.addAttribute("isAdminSede", isAdminSede);
        cargarCatalogos(model);
        return "usuarios-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuarioRequest") UsuarioRequest req, 
                          RedirectAttributes redirectAttributes, Principal principal) {
        try {
            var current = usuarioService.buscarPorUsername(principal.getName());
            if ("ADMIN_SEDE".equals(current.getRolNombre())) {
                req.setSucursalId(current.getSucursalId()); // Ensure they don't spoof the branch
            }

            usuarioService.crear(req);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario creado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/nuevo";
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // En un sistema real se verificaría que el admin sede solo borre de su sede.
        usuarioService.eliminar(id, 1L);
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario dado de baja con éxito.");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, Principal principal) {
        var current = usuarioService.buscarPorUsername(principal.getName());
        boolean isAdminSede = "ADMIN_SEDE".equals(current.getRolNombre());
        var usuario = usuarioService.buscarPorId(id);

        if (isAdminSede && !usuario.getSucursalId().equals(current.getSucursalId())) {
            return "redirect:/admin/usuarios"; // Security check
        }

        UsuarioRequest req = new UsuarioRequest();
        req.setUsername(usuario.getUsername());
        req.setNombreCompleto(usuario.getNombreCompleto());
        req.setDpi(usuario.getDpi());
        req.setNit(usuario.getNit());
        req.setTelefono(usuario.getTelefono());
        req.setNumeroSeguro(usuario.getNumeroSeguro());
        
        var rol = rolService.listarTodos().stream().filter(r -> r.getNombre().equals(usuario.getRolNombre())).findFirst().orElse(null);
        if (rol != null) req.setRolId(rol.getId());
        req.setSucursalId(usuario.getSucursalId());
        
        var especialidad = especialidadService.listarTodos().stream().filter(e -> e.getNombre().equals(usuario.getEspecialidadNombre())).findFirst().orElse(null);
        if (especialidad != null) req.setEspecialidadId(especialidad.getId());
        
        req.setActivo(usuario.isActivo());
        req.setPassword("password123");
        
        model.addAttribute("usuarioRequest", req);
        model.addAttribute("editMode", true);
        model.addAttribute("userId", id);
        model.addAttribute("isAdminSede", isAdminSede);
        cargarCatalogos(model);
        return "usuarios-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("usuarioRequest") UsuarioRequest req, 
                             RedirectAttributes redirectAttributes, Principal principal) {
        try {
            var current = usuarioService.buscarPorUsername(principal.getName());
            if ("ADMIN_SEDE".equals(current.getRolNombre())) {
                req.setSucursalId(current.getSucursalId()); // Force to own branch
            }

            usuarioService.actualizar(id, req);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/editar/" + id;
        }
        return "redirect:/admin/usuarios";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("sucursales", sucursalService.listarTodos());
        model.addAttribute("especialidades", especialidadService.listarTodos());
    }
}
