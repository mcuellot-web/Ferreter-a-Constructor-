package com.ferreteria.api.dto;

import com.ferreteria.api.model.Producto;
import java.math.BigDecimal;

public record ProductoResponse(Long id, String sku, String nombre, String descripcion, BigDecimal precioCosto,
                               BigDecimal precioVenta, int stock, int stockMinimo, boolean activo,
                               Long categoriaId, String categoria, Long proveedorId, String proveedor) {
    public static ProductoResponse from(Producto p) {
        return new ProductoResponse(p.getId(), p.getSku(), p.getNombre(), p.getDescripcion(), p.getPrecioCosto(),
                p.getPrecioVenta(), p.getStock(), p.getStockMinimo(), p.isActivo(),
                p.getCategoria().getId(), p.getCategoria().getNombre(),
                p.getProveedor() == null ? null : p.getProveedor().getId(),
                p.getProveedor() == null ? null : p.getProveedor().getNombre());
    }
}
