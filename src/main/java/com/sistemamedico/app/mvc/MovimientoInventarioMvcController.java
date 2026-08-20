package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.MovimientoInventarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventario/movimientos")
public class MovimientoInventarioMvcController {

    private final MovimientoInventarioService movimientoInventarioService;
    private final com.sistemamedico.app.repository.SucursalRepository sucursalRepository;
    private final com.sistemamedico.app.repository.MedicamentoRepository medicamentoRepository;

    public MovimientoInventarioMvcController(MovimientoInventarioService movimientoInventarioService,
                                             com.sistemamedico.app.repository.SucursalRepository sucursalRepository,
                                             com.sistemamedico.app.repository.MedicamentoRepository medicamentoRepository) {
        this.movimientoInventarioService = movimientoInventarioService;
        this.sucursalRepository = sucursalRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @GetMapping
    public String listarMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoInventarioService.listarTodos());
        model.addAttribute("sucursales", sucursalRepository.findAll());
        model.addAttribute("medicamentos", medicamentoRepository.findAll());
        return "movimientos-inventario";
    }

    @org.springframework.web.bind.annotation.PostMapping("/guardar")
    public String guardarAjuste(
            @org.springframework.web.bind.annotation.RequestParam Long sucursalId,
            @org.springframework.web.bind.annotation.RequestParam Long medicamentoId,
            @org.springframework.web.bind.annotation.RequestParam com.sistemamedico.app.model.MovimientoInventario.TipoMovimiento tipo,
            @org.springframework.web.bind.annotation.RequestParam Integer cantidad,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String referencia,
            @org.springframework.web.bind.annotation.RequestParam String motivo,
            org.springframework.security.core.Authentication authentication,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        try {
            movimientoInventarioService.registrarAjuste(sucursalId, medicamentoId, tipo, cantidad, referencia, motivo, authentication.getName());
            redirectAttributes.addFlashAttribute("mensajeExito", "Movimiento de inventario registrado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorNegocio", e.getMessage());
        }
        return "redirect:/inventario/movimientos";
    }
}