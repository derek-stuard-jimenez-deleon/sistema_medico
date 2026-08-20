package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.CitaService;
import com.sistemamedico.app.repository.PacienteRepository;
import com.sistemamedico.app.dto.CitaResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class RecepcionMvcController {

    private final CitaService citaService;
    private final PacienteRepository pacienteRepository;

    public RecepcionMvcController(CitaService citaService, PacienteRepository pacienteRepository) {
        this.citaService = citaService;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/recepcion")
    public String buscar(
            @RequestParam(required = false) String term,
            @RequestParam(required = false, defaultValue = "dpi") String tipoBusqueda,
            Model model) {
        
        if (term != null && !term.isBlank()) {
            model.addAttribute("termBuscado", term);
            model.addAttribute("tipoBusqueda", tipoBusqueda);

            if ("citaId".equals(tipoBusqueda)) {
                try {
                    CitaResponse cita = citaService.buscarPorId(Long.parseLong(term));
                    model.addAttribute("citas", List.of(cita));
                } catch (Exception e) {
                    model.addAttribute("citas", Collections.emptyList());
                }
            } else {
                List<CitaResponse> citas = citaService.buscarPorPaciente(term);
                model.addAttribute("citas", citas);
                
                if (citas.isEmpty()) {
                    boolean pacienteExiste = pacienteRepository.existsByDpi(term);
                    model.addAttribute("pacienteExiste", pacienteExiste);
                }
            }
        }
        return "recepcion";
    }

    @PostMapping("/recepcion/{id}/verificar")
    public String verificar(@PathVariable Long id, @RequestParam String term, @RequestParam String tipoBusqueda, RedirectAttributes redirectAttributes) {
        try {
            citaService.marcarVerificada(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Llegada del paciente verificada correctamente.");
        } catch (IllegalArgumentException | com.sistemamedico.app.exception.RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        return "redirect:/recepcion?term=" + term + "&tipoBusqueda=" + tipoBusqueda;
    }
    
    @PostMapping("/recepcion/{id}/emergencia")
    public String emergencia(@PathVariable Long id, @RequestParam String term, @RequestParam String tipoBusqueda, RedirectAttributes redirectAttributes) {
        try {
            citaService.marcarVerificadaEmergencia(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Paciente registrado con prioridad de EMERGENCIA. Debe pasar directamente a toma de signos vitales.");
        } catch (IllegalArgumentException | com.sistemamedico.app.exception.RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        return "redirect:/recepcion?term=" + term + "&tipoBusqueda=" + tipoBusqueda;
    }
}