package com.sistemamedico.app.mvc;

import com.sistemamedico.app.dto.CitaResponse;
import com.sistemamedico.app.dto.PagoRequest;
import com.sistemamedico.app.dto.PagoResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.repository.UsuarioRepository;
import com.sistemamedico.app.service.CitaService;
import com.sistemamedico.app.service.PagoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/caja/citas")
public class CajaCitasMvcController {

    private final CitaService citaService;
    private final PagoService pagoService;
    private final UsuarioRepository usuarioRepository;

    public CajaCitasMvcController(CitaService citaService, PagoService pagoService, UsuarioRepository usuarioRepository) {
        this.citaService = citaService;
        this.pagoService = pagoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String buscar(@RequestParam(required = false) String dpi, Model model) {
        if (dpi != null && !dpi.isBlank()) {
            try {
                // Por ahora, solo buscamos citas por DPI
                List<CitaResponse> citas = citaService.buscarPorPaciente(dpi)
                        .stream()
                        .filter(c -> "RESERVADA".equals(c.getEstado()) || "PENDIENTE_PAGO".equals(c.getEstado()))
                        .toList();
                model.addAttribute("citas", citas);
            } catch (Exception e) {
                model.addAttribute("errorNegocio", "No se encontraron resultados o el DPI es inválido.");
            }
            model.addAttribute("dpiBuscado", dpi);
        }
        return "caja-citas"; // Vista que crearemos en el siguiente paso
    }

    @PostMapping("/pagar")
    public String pagar(@RequestParam Long citaId,
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

            var cita = citaService.buscarPorId(citaId);
            if (!"RESERVADA".equals(cita.getEstado()) && !"PENDIENTE_PAGO".equals(cita.getEstado())) {
                throw new IllegalArgumentException("La cita indicada no está pendiente de pago.");
            }

            PagoRequest pagoRequest = new PagoRequest();
            pagoRequest.setTipoOrigen("CITA");
            pagoRequest.setReferenciaId(citaId);
            // NOTA: El monto de la cita debería venir de la especialidad o un catálogo. Usaremos un valor fijo por ahora.
            pagoRequest.setMonto(new BigDecimal("150.00")); 
            pagoRequest.setMetodoPago(metodoPago);
            pagoRequest.setMontoRecibido(montoRecibido);
            pagoRequest.setUltimosDigitosTarjeta(ultimosDigitosTarjeta);
            pagoRequest.setCajeroId(cajero.getId());
            pagoRequest.setSucursalId(cajero.getSucursal().getId());

            PagoResponse pagoResponse = pagoService.crear(pagoRequest);

            session.setAttribute("pago", pagoResponse);
            session.setAttribute("cita", cita);

            return "redirect:/caja/citas/comprobante";
        } catch (IllegalArgumentException | RecursoNoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
            return "redirect:/caja/citas?dpi=" + redirectAttributes.getAttribute("dpiBuscado");
        }
    }

    @GetMapping("/comprobante")
    public String comprobante(HttpSession session, Model model) {
        PagoResponse pago = (PagoResponse) session.getAttribute("pago");
        CitaResponse cita = (CitaResponse) session.getAttribute("cita");

        if (pago == null || cita == null) {
            return "redirect:/caja/citas";
        }

        model.addAttribute("pago", pago);
        model.addAttribute("cita", cita);

        session.removeAttribute("pago");
        session.removeAttribute("cita");

        return "comprobante-cita"; // Vista que crearemos después
    }
}