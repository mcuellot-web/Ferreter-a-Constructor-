package com.ferreteria.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** stockInicial solo se usa al crear; después el stock cambia con ventas y entradas. */
public record ProductoRequest(
        @NotBlank(message = "El SKU es obligatorio") @Size(max = 40) String sku,
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 150) String nombre,
        @Size(max = 255) String descripcion,
        @NotNull(message = "El precio de costo es obligatorio") @DecimalMin("0.00") BigDecimal precioCosto,
        @NotNull(message = "El precio de venta es obligatorio") @DecimalMin(value = "0.01") BigDecimal precioVenta,
        @Min(0) int stockInicial,
        @Min(0) int stockMinimo,
        @NotNull(message = "La categoría es obligatoria") Long categoriaId,
        Long proveedorId) {
}
