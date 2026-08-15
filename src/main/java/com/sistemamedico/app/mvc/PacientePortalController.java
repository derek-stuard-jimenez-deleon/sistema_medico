package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.CitaRequest; // Importar CitaRequest
import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.dto.UsuarioResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Paciente; // Importar Paciente
import com.sistemamedico.app.repository.PacienteRepository; // Importar PacienteRepository
import com.sistemamedico.app.service.CitaService;
import com.sistemamedico.app.service.EspecialidadService;
import com.sistemamedico.app.service.UsuarioService;
import jakarta.validation.Valid; // Importar @Valid
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Importar BindingResult
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // Importar @ModelAttribute
import org.springframework.web.bind.annotation.PostMapping; // Importar @PostMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importar RedirectAttributes
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

import java.time.LocalDate;
import java.time.LocalDateTime; // Importar LocalDateTime
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/paciente/portal")
public class PacientePortalController {

    private static final Logger logger = LoggerFactory.getLogger(PacientePortalController.class); // Añadir Logger

    private final PacienteRepository pacienteRepository;
    private final CitaService citaService;
    private final EspecialidadService especialidadService;
    private final UsuarioService usuarioService;

    public PacientePortalController(PacienteRepository pacienteRepository, CitaService citaService, EspecialidadService especialidadService, UsuarioService usuarioService) {
        this.pacienteRepository = pacienteRepository;
        this.citaService = citaService;
        this.especialidadService = especialidadService;
        this.usuarioService = usuarioService;
    }

    // Eliminado el método @InitBinder

    @GetMapping("/citas")
    public String verMisCitas(Authentication authentication, Model model) {
        String username = authentication.getName();

        try {
            var paciente = pacienteRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado para el usuario: " + username));

            List<CitaResponse> citas = citaService.buscarPorPaciente(paciente.getDpi());
            model.addAttribute("citas", citas);
            model.addAttribute("pacienteNombre", paciente.getNombreCompleto());

        } catch (RecursoNoEncontradoException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("citas", Collections.emptyList());
        }

        return "paciente-citas";
    }

    @GetMapping("/citas/agendar")
    public String mostrarFormularioAgendarCita(Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodos()); // Corregido: listarTodos()
        if (!model.containsAttribute("citaRequest")) {
            model.addAttribute("citaRequest", new CitaRequest());
        }
        return "paciente-agendar-cita";
    }

    @PostMapping("/citas/agendar")
    public String procesarAgendarCita(@Valid @ModelAttribute("citaRequest") CitaRequest citaRequest,
                                      BindingResult bindingResult,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes,
                                      Model model) { // Añadir Model para errores de negocio

        logger.info("Iniciando procesarAgendarCita para el usuario: {}", authentication.getName());
        logger.debug("CitaRequest recibido: {}", citaRequest);

        // Mover la asignación de pacienteId y sucursalId aquí, antes de la validación del formulario
        String username = authentication.getName();
        try {
            Paciente paciente = pacienteRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado para el usuario: " + username));
            citaRequest.setPacienteId(paciente.getId());
            logger.debug("PacienteId asignado a citaRequest: {}", citaRequest.getPacienteId());

            UsuarioResponse medico = usuarioService.buscarPorId(citaRequest.getMedicoId());
            if (medico.getSucursalId() == null) {
                logger.error("Médico {} no tiene sucursal asignada.", medico.getId());
                throw new IllegalArgumentException("El médico seleccionado no tiene una sucursal asignada.");
            }
            citaRequest.setSucursalId(medico.getSucursalId());
            logger.debug("SucursalId asignado a citaRequest: {}", citaRequest.getSucursalId());

        } catch (RecursoNoEncontradoException | IllegalArgumentException e) {
            // Si hay un error al obtener paciente o médico, añadirlo a los errores de validación
            bindingResult.reject("global.error", e.getMessage()); // Añadir un error global
            // Si hay errores de validación, volvemos a mostrar el formulario
            // y pasamos las especialidades de nuevo
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            model.addAttribute("errorNegocio", e.getMessage()); // Mostrar el error en la alerta
            return "paciente-agendar-cita";
        }

        // Ahora que pacienteId y sucursalId están asignados, podemos re-validar si es necesario
        // O simplemente confiar en las validaciones del servicio si estos campos son críticos
        if (bindingResult.hasErrors()) { // Re-validar si se añadieron errores en el try-catch de arriba
            logger.warn("Errores de validación encontrados después de asignar IDs: {}", bindingResult.getAllErrors());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            return "paciente-agendar-cita";
        }

        logger.debug("Fecha y Hora de la cita: {}", citaRequest.getFechaHora());

        try {
            // Llamar al servicio para agendar la cita
            citaService.crear(citaRequest);
            logger.info("Cita agendada exitosamente para el paciente {} con el médico {}", citaRequest.getPacienteId(), citaRequest.getMedicoId());

            redirectAttributes.addFlashAttribute("mensajeExito", "¡Cita agendada exitosamente!");
            return "redirect:/paciente/portal/citas";

        } catch (RecursoNoEncontradoException | IllegalArgumentException e) {
            logger.error("Error de negocio al agendar cita: {}", e.getMessage());
            // Capturar errores de negocio (ej: médico no encontrado, horario no disponible)
            model.addAttribute("errorNegocio", e.getMessage());
            // Volver a cargar las especialidades y el citaRequest para que no se pierdan los datos
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            return "paciente-agendar-cita";
        } catch (Exception e) { // Capturar cualquier otra excepción inesperada
            logger.error("Error inesperado al procesar agendar cita: {}", e.getMessage(), e);
            model.addAttribute("errorNegocio", "Ocurrió un error inesperado al agendar la cita.");
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            return "paciente-agendar-cita";
        }
    }


    @GetMapping("/medicos-por-especialidad")
    @ResponseBody
    public List<UsuarioResponse> getMedicosPorEspecialidad(@RequestParam Long especialidadId) {
        return usuarioService.listarMedicosPorEspecialidad(especialidadId);
    }

    @GetMapping("/horarios-disponibles")
    @ResponseBody
    public List<Map<String, Object>> getHorariosDisponibles(@RequestParam Long medicoId, 
                                                             @RequestParam String fecha) { // Eliminamos especialidadId
        // Convertir la fecha string (YYYY-MM-DD) a LocalDate
        LocalDate fechaDate = LocalDate.parse(fecha);
        
        // Obtener horarios ocupados del CitaService
        List<LocalTime> horariosOcupados = citaService.buscarHorariosOcupados(medicoId, fechaDate);

        // Generar todos los horarios posibles (8:00 - 17:30 en intervalos de 30 min)
        List<Map<String, Object>> horariosDisponibles = new ArrayList<>();
        for (int hora = 8; hora < 18; hora++) {
            for (int minuto = 0; minuto < 60; minuto += 30) {
                LocalTime horarioPosible = LocalTime.of(hora, minuto);
                boolean disponible = !horariosOcupados.contains(horarioPosible);
                
                Map<String, Object> horarioMap = new HashMap<>();
                horarioMap.put("hora", horarioPosible.toString()); // Formato HH:MM
                horarioMap.put("disponible", disponible);
                
                horariosDisponibles.add(horarioMap);
            }
        }
        
        return horariosDisponibles;
    }
}