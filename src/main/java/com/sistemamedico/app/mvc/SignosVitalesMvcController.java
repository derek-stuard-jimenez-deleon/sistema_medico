package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.dto.SignosVitalesRequest;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.repository.UsuarioRepository;
import com.sistemamedico.app.service.CitaService;
import com.sistemamedico.app.service.SignosVitalesService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/enfermeria")
public class SignosVitalesMvcController {

    private final CitaService citaService;
    private final SignosVitalesService signosVitalesService;
    private final UsuarioRepository usuarioRepository;

    public SignosVitalesMvcController(CitaService citaService,
                                      SignosVitalesService signosVitalesService,
                                      UsuarioRepository usuarioRepository) {
        this.citaService = citaService;
        this.signosVitalesService = signosVitalesService;
        this.usuarioRepository = usuarioRepository;
    }

    // Bandeja: cola de trabajo automática para enfermería (Muestra pacientes de hoy esperando signos)
    @GetMapping
    public String bandeja(@RequestParam(required = false) Long citaEnAtencion, Model model) {
        // Obtenemos automáticamente todas las citas pendientes del día de hoy
        List<CitaResponse> citas = citaService.buscarCitasParaEnfermeria();

        model.addAttribute("citas", citas);
        model.addAttribute("citaEnAtencion", citaEnAtencion);

        return "enfermeria-signos-vitales";
    }

    // Nuevo endpoint para cuando el enfermero da click en "Llamar y Tomar Signos"
    @PostMapping("/{citaId}/llamar")
    public String llamarPaciente(@PathVariable Long citaId) {
        citaService.cambiarEstado(citaId, com.sistemamedico.app.model.Cita.EstadoCita.EN_SIGNOS_VITALES);
        return "redirect:/enfermeria?citaEnAtencion=" + citaId;
    }

    @PostMapping("/{citaId}/registrar")
    public String registrar(@PathVariable Long citaId,
                            @RequestParam Integer presionSistolica,
                            @RequestParam Integer presionDiastolica,
                            @RequestParam BigDecimal temperatura,
                            @RequestParam BigDecimal peso,
                            @RequestParam BigDecimal talla,
                            @RequestParam Integer frecuenciaCardiaca,
                            @RequestParam(defaultValue = "false") boolean esEmergencia,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            var enfermero = usuarioRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario en sesión."));

            SignosVitalesRequest request = new SignosVitalesRequest();
            request.setCitaId(citaId);
            request.setEnfermeroId(enfermero.getId());
            request.setPresionSistolica(presionSistolica);
            request.setPresionDiastolica(presionDiastolica);
            request.setTemperatura(temperatura);
            request.setPeso(peso);
            request.setTalla(talla);
            request.setFrecuenciaCardiaca(frecuenciaCardiaca);
            request.setEsEmergencia(esEmergencia);

            var resultado = signosVitalesService.crear(request);

            if (esEmergencia) {
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Signos vitales de emergencia registrados para " + resultado.getPacienteNombre() +
                                ". El paciente debe pasar directamente a consulta médica.");
            } else if (resultado.getAlertasClinicas() != null) {
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Signos vitales registrados correctamente. Atención: " + resultado.getAlertasClinicas());
            } else {
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Signos vitales del paciente " + resultado.getPacienteNombre() +
                                " registrados correctamente. El paciente puede regresar a la sala de espera.");
            }
        } catch (IllegalArgumentException | RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }

        // Ya no enviamos el DPI en la redirección, regresamos directamente a la cola limpia
        return "redirect:/enfermeria";
    }

    // Mantenemos este método por si lo usas internamente en el futuro para validaciones
    private boolean tieneSignosVitales(Long citaId) {
        try {
            signosVitalesService.buscarPorCita(citaId);
            return true;
        } catch (RecursoNoEncontradoException e) {
            return false;
        }
    }
}