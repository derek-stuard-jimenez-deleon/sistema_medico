package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.PacienteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PacienteMvcController {

    private final PacienteService pacienteService;

    public PacienteMvcController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/pacientes")
    public String listar(Model model) {
        var pagina = pacienteService.listarTodos(PageRequest.of(0, 20));
        model.addAttribute("pacientes", pagina.getContent());
        return "pacientes"; // busca templates/pacientes.html
    }
}