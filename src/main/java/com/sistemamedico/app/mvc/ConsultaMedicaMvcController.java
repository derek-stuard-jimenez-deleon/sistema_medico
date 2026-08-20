package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.dto.ConsultaMedicaRequest;
import com.sistemamedico.app.dto.SignosVitalesResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import com.sistemamedico.app.service.CitaService;
import com.sistemamedico.app.service.ConsultaMedicaService;
import com.sistemamedico.app.service.SignosVitalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/consulta-medica")
public class ConsultaMedicaMvcController {

    private final CitaService citaService;
    private final ConsultaMedicaService consultaMedicaService;
    private final SignosVitalesService signosVitalesService;
    private final UsuarioRepository usuarioRepository;

    @Autowired private ExamenLaboratorioRepository examenLaboratorioRepository;
    @Autowired private MedicamentoRepository medicamentoRepository;
    @Autowired private ConsultaMedicaRepository consultaMedicaRepository;
    @Autowired private OrdenLaboratorioRepository ordenLaboratorioRepository;
    @Autowired private DetalleOrdenLaboratorioRepository detalleOrdenLaboratorioRepository;
    @Autowired private ResultadoLaboratorioRepository resultadoLaboratorioRepository;
    @Autowired private RecetaMedicaRepository recetaMedicaRepository;
    @Autowired private DetalleRecetaRepository detalleRecetaRepository;
    @Autowired private CitaRepository citaRepository;

    public ConsultaMedicaMvcController(CitaService citaService,
                                       ConsultaMedicaService consultaMedicaService,
                                       SignosVitalesService signosVitalesService,
                                       UsuarioRepository usuarioRepository) {
        this.citaService = citaService;
        this.consultaMedicaService = consultaMedicaService;
        this.signosVitalesService = signosVitalesService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String bandeja(Authentication authentication, Model model) {
        var medico = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario en sesión."));

        Map<Long, SignosVitalesResponse> signosPorCita = new HashMap<>();
        Map<Long, List<OrdenLaboratorio>> ordenesPorCita = new HashMap<>();
        Map<Long, List<ResultadoLaboratorio>> resultadosPorOrden = new HashMap<>();

        List<CitaResponse> citas = citaService.listarTodas().stream()
                .filter(c -> medico.getId().equals(c.getMedicoId()))
                .filter(c -> "EN_ESPERA_CONSULTA".equals(c.getEstado()) || "PENDIENTE_CONSULTA_EMERGENCIA".equals(c.getEstado()))
                .filter(c -> !tieneConsultaCerrada(c.getId()))
                .filter(c -> {
                    SignosVitalesResponse signos = obtenerSignos(c.getId());
                    if (signos == null) return false;
                    signosPorCita.put(c.getId(), signos);
                    
                    // Obtener las órdenes de laboratorio COMPLETADAS de este paciente
                    try {
                        Cita citaEntity = citaRepository.findById(c.getId()).orElse(null);
                        if (citaEntity != null) {
                            List<OrdenLaboratorio> ordenesPaciente = ordenLaboratorioRepository.findByPacienteDpi(citaEntity.getPaciente().getDpi());
                            List<OrdenLaboratorio> ordenesCompletadas = ordenesPaciente.stream()
                                    .filter(o -> o.getEstado() == OrdenLaboratorio.EstadoOrden.COMPLETADA)
                                    .toList();
                            ordenesPorCita.put(c.getId(), ordenesCompletadas);
                            
                            // Cargar resultados para cada orden
                            for (OrdenLaboratorio ord : ordenesCompletadas) {
                                List<DetalleOrdenLaboratorio> detalles = detalleOrdenLaboratorioRepository.findByOrdenId(ord.getId());
                                List<ResultadoLaboratorio> res = new java.util.ArrayList<>();
                                for (DetalleOrdenLaboratorio det : detalles) {
                                    resultadoLaboratorioRepository.findByDetalleOrdenId(det.getId()).ifPresent(res::add);
                                }
                                resultadosPorOrden.put(ord.getId(), res);
                            }
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                    
                    return true;
                })
                .sorted(Comparator.comparing(
                        (CitaResponse c) -> Boolean.TRUE.equals(signosPorCita.get(c.getId()).getEsEmergencia()),
                        Comparator.reverseOrder()))
                .toList();

        model.addAttribute("citas", citas);
        model.addAttribute("signosPorCita", signosPorCita);
        model.addAttribute("ordenesPorCita", ordenesPorCita);
        model.addAttribute("resultadosPorOrden", resultadosPorOrden);
        
        // Agregar catálogos para los formularios
        model.addAttribute("examenesCatalogo", examenLaboratorioRepository.findAll());
        model.addAttribute("medicamentosCatalogo", medicamentoRepository.findAll());

        return "consulta-medica";
    }

    @PostMapping("/{citaId}/guardar")
    public String guardar(@PathVariable Long citaId,
                          @RequestParam String motivoConsulta,
                          @RequestParam String hallazgosClinicos,
                          @RequestParam(required = false) String diagnostico,
                          @RequestParam(required = false) String codigoCie10,
                          @RequestParam String planTratamiento,
                          @RequestParam(required = false) List<Long> examenesSeleccionados,
                          @RequestParam(required = false) List<Long> medicamentoIds,
                          @RequestParam(required = false) List<String> dosis,
                          @RequestParam(required = false) List<String> frecuencia,
                          @RequestParam(required = false) List<String> duracion,
                          @RequestParam(defaultValue = "false") boolean agendarSeguimiento,
                          @RequestParam(required = false) String fechaSeguimiento,
                          @RequestParam(required = false) String tipoSeguimiento,
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

            var response = consultaMedicaService.crear(request);
            
            // Buscar la entidad persistida (crear devuelve DTO con ID)
            ConsultaMedica consulta = consultaMedicaRepository.findById(response.getId()).get();
            Cita cita = citaRepository.findById(citaId).get();
            Paciente paciente = cita.getPaciente();

            // 1. Crear Orden de Laboratorio si se seleccionaron exámenes
            if (examenesSeleccionados != null && !examenesSeleccionados.isEmpty()) {
                OrdenLaboratorio ordenLab = new OrdenLaboratorio();
                ordenLab.setConsulta(consulta);
                ordenLab.setPaciente(paciente);
                ordenLab.setMedico(medico);
                ordenLab.setEstado(OrdenLaboratorio.EstadoOrden.PENDIENTE);
                
                BigDecimal total = BigDecimal.ZERO;
                List<ExamenLaboratorio> examenes = examenLaboratorioRepository.findAllById(examenesSeleccionados);
                for (ExamenLaboratorio ex : examenes) {
                    total = total.add(ex.getPrecio());
                }
                ordenLab.setMontoTotal(total);
                ordenLaboratorioRepository.save(ordenLab);
                
                for (ExamenLaboratorio ex : examenes) {
                    DetalleOrdenLaboratorio detalle = new DetalleOrdenLaboratorio();
                    detalle.setOrden(ordenLab);
                    detalle.setExamen(ex);
                    detalleOrdenLaboratorioRepository.save(detalle);
                }
            }

            // 2. Crear Receta Médica si se seleccionaron medicamentos
            if (medicamentoIds != null && !medicamentoIds.isEmpty()) {
                RecetaMedica receta = new RecetaMedica();
                receta.setConsulta(consulta);
                recetaMedicaRepository.save(receta);
                
                for (int i = 0; i < medicamentoIds.size(); i++) {
                    Long medId = medicamentoIds.get(i);
                    Medicamento med = medicamentoRepository.findById(medId).orElse(null);
                    if (med != null) {
                        DetalleReceta detalle = new DetalleReceta();
                        detalle.setReceta(receta);
                        detalle.setMedicamento(med);
                        detalle.setDosis(dosis.size() > i ? dosis.get(i) : "");
                        detalle.setFrecuencia(frecuencia.size() > i ? frecuencia.get(i) : "");
                        detalle.setDuracion(duracion.size() > i ? duracion.get(i) : "");
                        detalleRecetaRepository.save(detalle);
                    }
                }
            }

            // 3. Crear Cita de Seguimiento (CU-11)
            if (agendarSeguimiento && fechaSeguimiento != null && !fechaSeguimiento.isEmpty()) {
                java.time.LocalDateTime fHora = java.time.LocalDateTime.parse(fechaSeguimiento);
                if (fHora.isBefore(java.time.LocalDateTime.now())) {
                    throw new IllegalArgumentException("La fecha de la cita de seguimiento debe ser futura.");
                }
                Cita nuevaCita = new Cita();
                nuevaCita.setPaciente(paciente);
                nuevaCita.setMedico(medico);
                nuevaCita.setEspecialidad(cita.getEspecialidad());
                nuevaCita.setSucursal(cita.getSucursal());
                nuevaCita.setFechaHora(fHora);
                nuevaCita.setMotivoVisita("Cita de seguimiento generada por el médico en la cita #" + cita.getId());
                nuevaCita.setEstado(Cita.EstadoCita.RESERVADA);
                nuevaCita.setCitaOrigen(cita);
                nuevaCita.setTipoSeguimiento(tipoSeguimiento);
                citaRepository.save(nuevaCita);
            }

            redirectAttributes.addFlashAttribute("mensajeExito",
                    cerrar ? "Consulta cerrada exitosamente. Órdenes y recetas generadas." : "Consulta guardada temporalmente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        return "redirect:/consulta-medica";
    }

    private SignosVitalesResponse obtenerSignos(Long citaId) {
        try {
            return signosVitalesService.buscarPorCita(citaId);
        } catch (RecursoNoEncontradoException e) {
            return null;
        }
    }

    private boolean tieneConsultaCerrada(Long citaId) {
        // En una implementación completa esto debería verificar si la consulta existe y está cerrada
        // Por simplicidad, consideramos "cerrada" a cualquier consulta ya guardada (o según regla de negocio).
        // Adaptaremos temporalmente comprobando si la cita ya no está EN_ESPERA_CONSULTA
        return false; 
    }
}