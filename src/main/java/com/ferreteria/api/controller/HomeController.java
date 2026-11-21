package com.ferreteria.api.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Punto de entrada informativo para comprobar que el servicio está en línea. */
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> inicio() {
        return Map.of(
                "aplicacion", "Ferretería API",
                "estado", "OK",
                "documentacion", "/swagger-ui.html",
                "openapi", "/v3/api-docs");
    }
}
