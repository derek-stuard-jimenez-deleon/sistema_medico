package com.sistemamedico.app.mvc;

import com.sistemamedico.app.repository.PacienteRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public HomeController(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index"; // busca templates/index.html
    }

    @PostMapping("/verificar-dpi")
    public String verificarDpi(@RequestParam String dpi, RedirectAttributes redirectAttributes) {
        if (dpi == null || dpi.isBlank() || !dpi.matches("\\d{13}")) {
            redirectAttributes.addFlashAttribute("errorModal", "El DPI debe tener 13 dígitos numéricos.");
            return "redirect:/";
        }

        // Escenario 1: ¿El DPI pertenece a un paciente?
        if (pacienteRepository.existsByDpi(dpi)) {
            return "redirect:/login";
        }

        // Escenario 2: ¿El DPI pertenece a un usuario interno?
        if (usuarioRepository.existsByDpi(dpi)) {
            redirectAttributes.addFlashAttribute("errorModal", "Este DPI pertenece a un usuario interno. Use el acceso administrativo.");
            return "redirect:/";
        }

        // Escenario 3: El DPI no existe en ningún lado
        redirectAttributes.addFlashAttribute("infoModal", "No se encontró un registro. Por favor, regístrese para continuar.");
        redirectAttributes.addFlashAttribute("dpi", dpi); // Pasamos el DPI al formulario de registro
        return "redirect:/registro";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}