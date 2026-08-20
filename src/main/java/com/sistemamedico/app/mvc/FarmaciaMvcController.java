package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.DespachoMedicamentoRequest;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.RecetaMedica;
import com.sistemamedico.app.model.Usuario;
import com.sistemamedico.app.repository.RecetaMedicaRepository;
import com.sistemamedico.app.repository.UsuarioRepository;
import com.sistemamedico.app.service.DespachoMedicamentoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/farmacia")
public class FarmaciaMvcController {

    private final DespachoMedicamentoService despachoService;
    private final RecetaMedicaRepository recetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final com.sistemamedico.app.repository.DetalleRecetaRepository detalleRecetaRepository;

    public FarmaciaMvcController(DespachoMedicamentoService despachoService,
                                 RecetaMedicaRepository recetaRepository,
                                 UsuarioRepository usuarioRepository,
                                 com.sistemamedico.app.repository.DetalleRecetaRepository detalleRecetaRepository) {
        this.despachoService = despachoService;
        this.recetaRepository = recetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @GetMapping
    public String buscarReceta(@RequestParam(required = false) String dpi, Model model) {
        if (dpi != null && !dpi.isBlank()) {
            model.addAttribute("dpiBuscado", dpi);
            // Buscamos la receta más reciente del paciente
            recetaRepository.findFirstByConsultaCitaPacienteDpiOrderByIdDesc(dpi)
                    .ifPresentOrElse(
                            receta -> {
                                model.addAttribute("receta", receta);
                                model.addAttribute("detalles", detalleRecetaRepository.findByRecetaId(receta.getId()));
                            },
                            () -> model.addAttribute("errorNegocio", "No se encontró ninguna receta reciente para el DPI ingresado.")
                    );
        }
        return "farmacia";
    }

    @PostMapping("/despachar")
    public String despachar(@RequestParam Long recetaId,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            Usuario farmaceutico = usuarioRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado en sesión."));

            DespachoMedicamentoRequest request = new DespachoMedicamentoRequest();
            request.setRecetaId(recetaId);
            request.setFarmaceuticoId(farmaceutico.getId());
            request.setSucursalId(1L); 

            var response = despachoService.crear(request);

            redirectAttributes.addFlashAttribute("mensajeExito", 
                "Despacho registrado exitosamente.");

        } catch (Exception e) {
            String msg = e.getMessage();
            if (e.getCause() != null) {
                msg += " | Cause: " + e.getCause().getMessage();
                if (e.getCause().getCause() != null) {
                    msg += " | Root: " + e.getCause().getCause().getMessage();
                }
            }
            redirectAttributes.addFlashAttribute("errorNegocio", msg);
        }

        return "redirect:/farmacia";
    }
}
