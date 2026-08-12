package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.MovimientoInventarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovimientoInventarioMvcController {

    private final MovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioMvcController(MovimientoInventarioService movimientoInventarioService) {
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @GetMapping("/inventario/movimientos")
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoInventarioService.listarTodos());
        return "movimientos-inventario";
    }
}