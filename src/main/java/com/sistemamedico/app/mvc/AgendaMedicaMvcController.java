package com.sistemamedico.app.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agenda")
public class AgendaMedicaMvcController {

    @GetMapping
    public String verAgenda() {
        return "agenda-medica";
    }
}
