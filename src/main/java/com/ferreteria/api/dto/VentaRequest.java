package com.ferreteria.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VentaRequest(
        Long clienteId,
        @NotEmpty(message = "La venta debe tener al menos un ítem") List<@Valid ItemVentaRequest> items) {
}
