package com.sistemamedico.app.mvc;

import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/laboratorio")
@PreAuthorize("hasAnyRole('LABORATORISTA', 'ADMIN')")
public class LaboratorioMvcController {

    @Autowired
    private OrdenLaboratorioRepository ordenRepository;

    @Autowired
    private DetalleOrdenLaboratorioRepository detalleOrdenRepository;

    @Autowired
    private ResultadoLaboratorioRepository resultadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String bandeja(Model model) {
        List<OrdenLaboratorio> ordenes = ordenRepository.findAll();
        ordenes.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        
        model.addAttribute("ordenes", ordenes);
        return "laboratorio";
    }

    @GetMapping("/orden/{id}")
    public String detalleOrden(@PathVariable Long id, Model model) {
        OrdenLaboratorio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        List<DetalleOrdenLaboratorio> detalles = detalleOrdenRepository.findByOrdenId(id);
        
        // Crear un mapa de resultados para fácil acceso en Thymeleaf
        Map<Long, ResultadoLaboratorio> mapaResultados = new HashMap<>();
        for (DetalleOrdenLaboratorio det : detalles) {
            resultadoRepository.findByDetalleOrdenId(det.getId())
                    .ifPresent(res -> mapaResultados.put(det.getId(), res));
        }
        
        model.addAttribute("orden", orden);
        model.addAttribute("detalles", detalles);
        model.addAttribute("mapaResultados", mapaResultados);
        
        return "laboratorio-detalle";
    }

    @PostMapping("/orden/{ordenId}/resultado")
    public String guardarResultado(@PathVariable Long ordenId,
                                   @RequestParam Long detalleId,
                                   @RequestParam BigDecimal valor,
                                   @RequestParam String unidadMedida,
                                   @RequestParam(defaultValue = "false") boolean fueraDeRango,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        try {
            DetalleOrdenLaboratorio detalle = detalleOrdenRepository.findById(detalleId)
                    .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
            
            if (!detalle.getOrden().getId().equals(ordenId)) {
                throw new RuntimeException("El detalle no pertenece a esta orden");
            }
            
            if (detalle.getOrden().getEstado() != OrdenLaboratorio.EstadoOrden.EN_PROCESO) {
                throw new RuntimeException("La orden debe estar EN PROCESO para registrar resultados (verifique que el paciente haya pagado en caja).");
            }

            ResultadoLaboratorio resultado = resultadoRepository.findByDetalleOrdenId(detalleId)
                    .orElse(new ResultadoLaboratorio());
            
            resultado.setDetalleOrden(detalle);
            resultado.setValor(valor);
            resultado.setUnidadMedida(unidadMedida);
            resultado.setFueraDeRango(fueraDeRango);
            
            if (resultado.isPublicado()) {
                throw new RuntimeException("El resultado ya fue publicado y no se puede modificar.");
            }
            
            resultadoRepository.save(resultado);
            
            redirectAttributes.addFlashAttribute("mensajeExito", "Resultado guardado exitosamente para " + detalle.getExamen().getNombre());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        
        return "redirect:/laboratorio/orden/" + ordenId;
    }
    
    @PostMapping("/orden/{ordenId}/publicar")
    public String publicarResultado(@PathVariable Long ordenId,
                                    @RequestParam Long detalleId,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        try {
            ResultadoLaboratorio resultado = resultadoRepository.findByDetalleOrdenId(detalleId)
                    .orElseThrow(() -> new RuntimeException("Primero debe guardar el resultado antes de publicarlo."));
            
            if (resultado.isPublicado()) {
                throw new RuntimeException("El resultado ya estaba publicado.");
            }
            
            Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElse(null);
            
            resultado.setPublicado(true);
            resultado.setValidadoPor(usuario);
            resultado.setFechaValidacion(java.time.LocalDateTime.now());
            resultadoRepository.save(resultado);
            
            // Verificar si todos están publicados
            List<DetalleOrdenLaboratorio> detalles = detalleOrdenRepository.findByOrdenId(ordenId);
            boolean todosPublicados = true;
            for (DetalleOrdenLaboratorio d : detalles) {
                Optional<ResultadoLaboratorio> r = resultadoRepository.findByDetalleOrdenId(d.getId());
                if (r.isEmpty() || !r.get().isPublicado()) {
                    todosPublicados = false;
                    break;
                }
            }
            
            if (todosPublicados) {
                OrdenLaboratorio orden = ordenRepository.findById(ordenId).get();
                orden.setEstado(OrdenLaboratorio.EstadoOrden.COMPLETADA);
                ordenRepository.save(orden);
                redirectAttributes.addFlashAttribute("mensajeExito", "Resultado publicado. ¡Todos los exámenes han sido publicados! Orden completada.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeExito", "Resultado publicado exitosamente.");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        
        return "redirect:/laboratorio/orden/" + ordenId;
    }
}
