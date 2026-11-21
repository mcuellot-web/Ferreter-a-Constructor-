package com.ferreteria.api.dto;

import com.ferreteria.api.model.Cliente;

public record ClienteResponse(Long id, String nombre, String documento, String telefono, String email) {
    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNombre(), c.getDocumento(), c.getTelefono(), c.getEmail());
    }
}
