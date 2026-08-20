package com.sistemamedico.app.mvc;

import com.sistemamedico.app.repository.InventarioMedicamentoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventario")
public class InventarioMedicamentoMvcController {

    private final InventarioMedicamentoRepository inventarioRepository;

    public InventarioMedicamentoMvcController(InventarioMedicamentoRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @GetMapping
    public String listarInventario(Model model) {
        model.addAttribute("inventarios", inventarioRepository.findAllWithDetails());
        return "inventario-actual";
    }
}
