package com.ferreteria.api.dto;

import com.ferreteria.api.model.Proveedor;

public record ProveedorResponse(Long id, String nombre, String nit, String telefono, String email) {
    public static ProveedorResponse from(Proveedor p) {
        return new ProveedorResponse(p.getId(), p.getNombre(), p.getNit(), p.getTelefono(), p.getEmail());
    }
}
