package com.sistemamedico.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // Importar EnableScheduling

@SpringBootApplication
@EnableScheduling // Habilitar la programación de tareas
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}