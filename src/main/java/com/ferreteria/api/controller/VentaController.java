package com.ferreteria.api.controller;

import com.ferreteria.api.dto.VentaRequest;
import com.ferreteria.api.dto.VentaResponse;
import com.ferreteria.api.service.VentaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponse registrar(@Valid @RequestBody VentaRequest request) {
        return service.registrar(request);
    }

    @GetMapping
    public List<VentaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}
