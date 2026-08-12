package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.OrdenLaboratorioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CajaLaboratorioMvcController {

    private final OrdenLaboratorioService ordenLaboratorioService;

    public CajaLaboratorioMvcController(OrdenLaboratorioService ordenLaboratorioService) {
        this.ordenLaboratorioService = ordenLaboratorioService;
    }

    @GetMapping("/caja/laboratorio")
    public String buscar(@RequestParam(required = false) String dpi, Model model) {
        if (dpi != null && !dpi.isBlank()) {
            model.addAttribute("ordenes", ordenLaboratorioService.buscarPendientesPorDpi(dpi));
            model.addAttribute("dpiBuscado", dpi);
        }
        return "caja-laboratorio";
    }
}