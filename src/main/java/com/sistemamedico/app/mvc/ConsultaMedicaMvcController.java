package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.dto.ConsultaMedicaRequest;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.repository.UsuarioRepository;
import com.sistemamedico.app.service.CitaService;
import com.sistemamedico.app.service.ConsultaMedicaService;
import com.sistemamedico.app.service.SignosVitalesService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/consulta-medica")
public class ConsultaMedicaMvcController {

    private final CitaService citaService;
    private final ConsultaMedicaService consultaMedicaService;
    private final SignosVitalesService signosVitalesService;
    private final UsuarioRepository usuarioRepository;

    public ConsultaMedicaMvcController(CitaService citaService,
                                       ConsultaMedicaService consultaMedicaService,
                                       SignosVitalesService signosVitalesService,
                                       UsuarioRepository usuarioRepository) {
        this.citaService = citaService;
        this.consultaMedicaService = consultaMedicaService;
        this.signosVitalesService = signosVitalesService;
        this.usuarioRepository = usuarioRepository;
    }

    // Bandeja del médico logueado: sus citas PAGADAS con signos vitales ya tomados, aún sin consulta cerrada
    @GetMapping
    public String bandeja(Authentication authentication, Model model) {
        var medico = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario en sesión."));

        List<CitaResponse> citas = citaService.listarTodas().stream()
                .filter(c -> medico.getId().equals(c.getMedicoId()))
                .filter(c -> "PAGADA".equals(c.getEstado()))
                .filter(c -> tieneSignosVitales(c.getId()))
                .filter(c -> !tieneConsultaCerrada(c.getId()))
                .toList();

        model.addAttribute("citas", citas);
        return "consulta-medica";
    }

    @PostMapping("/{citaId}/guardar")
    public String guardar(@PathVariable Long citaId,
                          @RequestParam String motivoConsulta,
                          @RequestParam String hallazgosClinicos,
                          @RequestParam(required = false) String diagnostico,
                          @RequestParam(required = false) String codigoCie10,
                          @RequestParam String planTratamiento,
                          @RequestParam(defaultValue = "false") boolean cerrar,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            var medico = usuarioRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario en sesión."));

            ConsultaMedicaRequest request = new ConsultaMedicaRequest();
            request.setCitaId(citaId);
            request.setMedicoId(medico.getId());
            request.setMotivoConsulta(motivoConsulta);
            request.setHallazgosClinicos(hallazgosClinicos);
            request.setDiagnostico(diagnostico);
            request.setCodigoCie10(codigoCie10);
            request.setPlanTratamiento(planTratamiento);
            request.setCerrar(cerrar);

            consultaMedicaService.crear(request);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    cerrar ? "Consulta cerrada. La cita fue marcada como ATENDIDA." : "Consulta guardada.");
        } catch (IllegalArgumentException | RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        return "redirect:/consulta-medica";
    }

    private boolean tieneSignosVitales(Long citaId) {
        try {
            signosVitalesService.buscarPorCita(citaId);
            return true;
        } catch (RecursoNoEncontradoException e) {
            return false;
        }
    }

    private boolean tieneConsultaCerrada(Long citaId) {
        try {
            var consulta = consultaMedicaService.buscarPorCita(citaId);
            return "CERRADA".equals(consulta.getEstado());
        } catch (RecursoNoEncontradoException e) {
            return false;
        }
    }
}