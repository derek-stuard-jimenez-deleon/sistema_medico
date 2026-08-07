package com.sistemamedico.app.mvc;

import com.sistemamedico.app.service.UsuarioService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioMvcController {

    private final UsuarioService usuarioService;

    public UsuarioMvcController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public String listar(Model model) {
        var pagina = usuarioService.listarTodos(PageRequest.of(0, 20));
        model.addAttribute("usuarios", pagina.getContent());
        return "usuarios"; // busca templates/usuarios.html
    }
}