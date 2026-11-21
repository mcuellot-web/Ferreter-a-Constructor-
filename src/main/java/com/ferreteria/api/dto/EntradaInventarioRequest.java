package com.ferreteria.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EntradaInventarioRequest(
        @NotNull(message = "El producto es obligatorio") Long productoId,
        @Min(value = 1, message = "La cantidad debe ser al menos 1") int cantidad,
        Long proveedorId,
        @Size(max = 255) String observacion) {
}
