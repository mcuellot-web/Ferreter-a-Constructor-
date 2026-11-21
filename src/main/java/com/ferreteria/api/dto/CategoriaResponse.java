package com.ferreteria.api.dto;

import com.ferreteria.api.model.Categoria;

public record CategoriaResponse(Long id, String nombre, String descripcion) {
    public static CategoriaResponse from(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
