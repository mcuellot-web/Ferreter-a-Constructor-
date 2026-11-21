package com.ferreteria.api.controller;

import com.ferreteria.api.dto.EntradaInventarioRequest;
import com.ferreteria.api.dto.EntradaInventarioResponse;
import com.ferreteria.api.service.InventarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    public EntradaInventarioResponse registrarEntrada(@Valid @RequestBody EntradaInventarioRequest request) {
        return service.registrarEntrada(request);
    }

    @GetMapping("/productos/{productoId}/entradas")
    public List<EntradaInventarioResponse> historial(@PathVariable Long productoId) {
        return service.historialPorProducto(productoId);
    }
}
