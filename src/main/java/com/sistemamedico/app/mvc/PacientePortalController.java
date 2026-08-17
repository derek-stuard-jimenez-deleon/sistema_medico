package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.CitaRequest; // Importar CitaRequest
import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.dto.UsuarioResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.Cita; // Importar Cita para EstadoCita
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
import org.springframework.web.bind.annotation.PathVariable; // Importar @PathVariable
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
        Paciente paciente = null; // Declarar paciente aquí
        try {
            paciente = pacienteRepository.findByUsername(username)
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
            CitaResponse citaCreada = citaService.crear(citaRequest); // Capturar la respuesta
            logger.info("Cita agendada exitosamente para el paciente {} con el médico {}", citaRequest.getPacienteId(), citaRequest.getMedicoId());

            redirectAttributes.addFlashAttribute("mensajeExito", "¡Cita agendada exitosamente! Ahora complete el pago.");
            return "redirect:/paciente/portal/citas/" + citaCreada.getId() + "/pagar"; // Redirigir a la página de pago

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

    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        logger.info("Intento de cancelación de cita {} por el usuario {}", id, username);

        try {
            Paciente paciente = pacienteRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado para el usuario: " + username));

            // Llama al servicio para cancelar la cita, pasando el ID de la cita y el ID del paciente para verificar propiedad
            citaService.cancelarCita(id, paciente.getId());

            redirectAttributes.addFlashAttribute("mensajeExito", "Cita #" + id + " cancelada exitosamente.");
            return "redirect:/paciente/portal/citas";

        } catch (RecursoNoEncontradoException | IllegalArgumentException e) {
            logger.error("Error al cancelar cita {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/paciente/portal/citas";
        } catch (Exception e) {
            logger.error("Error inesperado al cancelar cita {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado al cancelar la cita.");
            return "redirect:/paciente/portal/citas";
        }
    }

    @GetMapping("/citas/reagendar/{id}")
    public String mostrarFormularioReagendarCita(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        logger.info("Mostrando formulario de reagendamiento para cita {} por el usuario {}", id, username);

        try {
            Paciente paciente = pacienteRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado para el usuario: " + username));

            CitaResponse citaOriginal = citaService.buscarPorId(id);

            // Verificar que la cita pertenezca al paciente
            if (!citaOriginal.getPacienteDpi().equals(paciente.getDpi())) {
                throw new IllegalArgumentException("No tiene permisos para reagendar esta cita.");
            }
            // Verificar que la cita esté en estado reagendable (RESERVADA) y sea futura
            if (!citaOriginal.getEstado().equals(Cita.EstadoCita.RESERVADA.name()) || citaOriginal.getFechaHora().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Solo se pueden reagendar citas futuras en estado RESERVADA.");
            }

            // Pre-llenar el CitaRequest con los datos de la cita original
            CitaRequest citaRequest = new CitaRequest();
            citaRequest.setEspecialidadId(citaOriginal.getEspecialidadId()); // Corregido
            citaRequest.setMedicoId(citaOriginal.getMedicoId()); // Corregido
            citaRequest.setMotivoVisita(citaOriginal.getMotivoVisita());
            // La fecha y hora se seleccionarán de nuevo, no se pre-llenan directamente en el request

            model.addAttribute("citaRequest", citaRequest);
            model.addAttribute("citaOriginalId", id); // Para saber qué cita estamos reagendando
            model.addAttribute("especialidades", especialidadService.listarTodos());

            return "paciente-agendar-cita"; // Reutilizamos el mismo formulario
        } catch (RecursoNoEncontradoException | IllegalArgumentException e) {
            logger.error("Error al mostrar formulario de reagendamiento para cita {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/paciente/portal/citas";
        } catch (Exception e) {
            logger.error("Error inesperado al mostrar formulario de reagendamiento para cita {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado al mostrar el formulario de reagendamiento.");
            return "redirect:/paciente/portal/citas";
        }
    }

    @PostMapping("/citas/reagendar/{id}") // Nuevo método para procesar el reagendamiento
    public String procesarReagendarCita(@PathVariable Long id,
                                        @Valid @ModelAttribute("citaRequest") CitaRequest citaRequest,
                                        BindingResult bindingResult,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        logger.info("Iniciando procesarReagendarCita para cita {} por el usuario {}", id, authentication.getName());
        logger.debug("CitaRequest recibido para reagendamiento: {}", citaRequest);

        // Mover la asignación de pacienteId y sucursalId aquí, antes de la validación del formulario
        String username = authentication.getName();
        Paciente paciente = null; // Declarar paciente aquí
        try {
            paciente = pacienteRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado para el usuario: " + username));
            citaRequest.setPacienteId(paciente.getId());
            logger.debug("PacienteId asignado a citaRequest para reagendamiento: {}", citaRequest.getPacienteId());

            UsuarioResponse medico = usuarioService.buscarPorId(citaRequest.getMedicoId());
            if (medico.getSucursalId() == null) {
                logger.error("Médico {} no tiene sucursal asignada.", medico.getId());
                throw new IllegalArgumentException("El médico seleccionado no tiene una sucursal asignada.");
            }
            citaRequest.setSucursalId(medico.getSucursalId());
            logger.debug("SucursalId asignado a citaRequest para reagendamiento: {}", citaRequest.getSucursalId());

        } catch (RecursoNoEncontradoException | IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            model.addAttribute("citaOriginalId", id); // Mantener el ID original
            model.addAttribute("errorNegocio", e.getMessage());
            return "paciente-agendar-cita";
        }

        if (bindingResult.hasErrors()) {
            logger.warn("Errores de validación encontrados para reagendamiento: {}", bindingResult.getAllErrors());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            model.addAttribute("citaOriginalId", id); // Mantener el ID original
            return "paciente-agendar-cita";
        }

        logger.debug("Nueva Fecha y Hora de la cita para reagendamiento: {}", citaRequest.getFechaHora());

        try {
            citaService.reagendarCita(id, citaRequest, paciente.getId()); // Nuevo método en CitaService
            logger.info("Cita {} reagendada exitosamente por el paciente {}", id, paciente.getId());

            redirectAttributes.addFlashAttribute("mensajeExito", "¡Cita #" + id + " reagendada exitosamente!");
            return "redirect:/paciente/portal/citas";

        } catch (RecursoNoEncontradoException | IllegalArgumentException e) {
            logger.error("Error de negocio al reagendar cita {}: {}", id, e.getMessage());
            model.addAttribute("errorNegocio", e.getMessage());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            model.addAttribute("citaOriginalId", id); // Mantener el ID original
            return "paciente-agendar-cita";
        } catch (Exception e) {
            logger.error("Error inesperado al procesar reagendamiento de cita {}: {}", id, e.getMessage(), e);
            model.addAttribute("errorNegocio", "Ocurrió un error inesperado al reagendar la cita.");
            model.addAttribute("especialidades", especialidadService.listarTodos());
            model.addAttribute("citaRequest", citaRequest);
            model.addAttribute("citaOriginalId", id); // Mantener el ID original
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
        List<LocalTime> horariosOcupados = citaService.buscarHorariosOcupados(medicoId, fechaDate, Optional.empty());

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

    @GetMapping("/citas/{citaId}/pagar")
    public String mostrarFormularioPagoCita(@PathVariable Long citaId, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        try {
            Paciente paciente = pacienteRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado para el usuario: " + username));

            CitaResponse cita = citaService.buscarPorId(citaId);

            // Validar que la cita pertenezca al paciente autenticado
            if (!cita.getPacienteDpi().equals(paciente.getDpi())) {
                redirectAttributes.addFlashAttribute("error", "No tiene permisos para acceder a esta página de pago.");
                return "redirect:/paciente/portal/citas";
            }

            // Validar que la cita esté en estado RESERVADA (asumiendo que RESERVADA significa pendiente de pago inicial)
            if (!cita.getEstado().equals(Cita.EstadoCita.RESERVADA.name())) {
                redirectAttributes.addFlashAttribute("error", "La cita no está en estado de pago pendiente.");
                return "redirect:/paciente/portal/citas";
            }

            model.addAttribute("cita", cita);
            // Aquí se podría añadir el tiempo de expiración de la reserva si se gestiona en el backend
            // Por ahora, el temporizador se iniciará en el frontend al cargar la página de pago.
            return "paciente-pago-cita"; // Nombre de la nueva plantilla HTML
        } catch (RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/paciente/portal/citas";
        } catch (Exception e) {
            logger.error("Error al mostrar formulario de pago para cita {}: {}", citaId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado al cargar la página de pago.");
            return "redirect:/paciente/portal/citas";
        }
    }
}