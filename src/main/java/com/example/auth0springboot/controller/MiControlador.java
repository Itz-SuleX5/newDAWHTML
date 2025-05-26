package com.example.auth0springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiControlador {
    private final MiServicio miServicio;

    @Autowired
    public MiControlador(MiServicio miServicio) {
        this.miServicio = miServicio;
    }

    @GetMapping("/pagina")
    public String mostrarPagina() {
        try {
            miServicio.realizarOperacion();
            return "dashboard"; // Puedes cambiarlo por la vista que desees
        } catch (Exception e) {
            return "error";
        }
    }
}
