package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CitaMvcController {

    private final CitaService citaService;

    public CitaMvcController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping("/citas")
    public String listar(Model model) {
        model.addAttribute("citas", citaService.listarTodas());
        return "citas"; // busca templates/citas.html
    }
}