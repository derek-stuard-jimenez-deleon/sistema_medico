package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.OrdenLaboratorioResponse;
import com.sistemamedico.app.dto.PagoRequest;
import com.sistemamedico.app.dto.PagoResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.repository.UsuarioRepository;
import com.sistemamedico.app.service.OrdenLaboratorioService;
import com.sistemamedico.app.service.PagoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/caja/laboratorio")
public class CajaLaboratorioMvcController {

    private final OrdenLaboratorioService ordenLaboratorioService;
    private final PagoService pagoService;
    private final UsuarioRepository usuarioRepository;

    public CajaLaboratorioMvcController(OrdenLaboratorioService ordenLaboratorioService,
                                        PagoService pagoService,
                                        UsuarioRepository usuarioRepository) {
        this.ordenLaboratorioService = ordenLaboratorioService;
        this.pagoService = pagoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String buscar(@RequestParam(required = false, defaultValue = "DPI") String criterio,
                         @RequestParam(required = false) String valor,
                         Model model) {
        if (valor != null && !valor.isBlank()) {
            List<OrdenLaboratorioResponse> ordenes = Collections.emptyList();
            try {
                if ("DPI".equals(criterio)) {
                    ordenes = ordenLaboratorioService.buscarPendientesPorDpi(valor);
                } else if ("ORDEN".equals(criterio)) {
                    OrdenLaboratorioResponse orden = ordenLaboratorioService.buscarPorId(Long.valueOf(valor));
                    if ("PENDIENTE".equals(orden.getEstado())) {
                        ordenes = List.of(orden);
                    }
                }
            } catch (RecursoNoEncontradoException e) {
                // No hacer nada, la lista de ordenes simplemente quedará vacía, que es el comportamiento esperado.
            } catch (NumberFormatException e) {
                model.addAttribute("errorNegocio", "El número de orden debe ser numérico.");
            }
            model.addAttribute("ordenes", ordenes);
            model.addAttribute("valorBuscado", valor);
        }
        model.addAttribute("criterio", criterio);
        return "caja-laboratorio";
    }

    @PostMapping("/pagar")
    public String pagar(@RequestParam Long ordenId,
                        @RequestParam String metodoPago,
                        @RequestParam(required = false) BigDecimal montoRecibido,
                        @RequestParam(required = false) String ultimosDigitosTarjeta,
                        Authentication authentication,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        try {
            var cajero = usuarioRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario de caja."));
            if (cajero.getSucursal() == null) {
                throw new IllegalArgumentException("El cajero no tiene una sucursal asignada.");
            }

            var orden = ordenLaboratorioService.buscarPorId(ordenId);
            if (!"PENDIENTE".equals(orden.getEstado())) {
                throw new IllegalArgumentException("La orden indicada no está pendiente de pago.");
            }

            PagoRequest pagoRequest = new PagoRequest();
            pagoRequest.setTipoOrigen("LABORATORIO");
            pagoRequest.setReferenciaId(ordenId);
            pagoRequest.setMonto(orden.getMontoTotal());
            pagoRequest.setMetodoPago(metodoPago);
            pagoRequest.setMontoRecibido(montoRecibido);
            pagoRequest.setUltimosDigitosTarjeta(ultimosDigitosTarjeta);
            pagoRequest.setCajeroId(cajero.getId());
            pagoRequest.setSucursalId(cajero.getSucursal().getId());

            PagoResponse pagoResponse = pagoService.crear(pagoRequest);

            // Guardar en la sesión en lugar de RedirectAttributes
            session.setAttribute("pago", pagoResponse);
            session.setAttribute("orden", orden);

            return "redirect:/caja/laboratorio/comprobante";
        } catch (IllegalArgumentException | RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
            return "redirect:/caja/laboratorio?criterio=ORDEN&valor=" + ordenId;
        }
    }

    @GetMapping("/comprobante")
    public String comprobante(HttpSession session, Model model, SessionStatus status) {
        PagoResponse pago = (PagoResponse) session.getAttribute("pago");
        OrdenLaboratorioResponse orden = (OrdenLaboratorioResponse) session.getAttribute("orden");

        if (pago == null || orden == null) {
            // Si no hay nada en la sesión, redirigir a la página principal de caja
            return "redirect:/caja/laboratorio";
        }

        model.addAttribute("pago", pago);
        model.addAttribute("orden", orden);

        // Limpiar los atributos de la sesión para que no se queden "pegados"
        session.removeAttribute("pago");
        session.removeAttribute("orden");

        return "comprobante-laboratorio";
    }
}