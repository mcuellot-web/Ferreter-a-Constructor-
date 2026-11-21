package com.ferreteria.api.controller;

import com.ferreteria.api.dto.ProveedorRequest;
import com.ferreteria.api.dto.ProveedorResponse;
import com.ferreteria.api.service.CrudService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final CrudService<ProveedorRequest, ProveedorResponse> service;

    public ProveedorController(CrudService<ProveedorRequest, ProveedorResponse> service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProveedorResponse crear(@Valid @RequestBody ProveedorRequest request) {
        return service.crear(request);
    }

    @GetMapping
    public List<ProveedorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProveedorResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    public ProveedorResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
