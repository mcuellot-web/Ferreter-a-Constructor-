package com.ferreteria.api.dto;

import com.ferreteria.api.model.MovimientoInventario;
import java.time.LocalDateTime;

public record EntradaInventarioResponse(Long id, Long productoId, String producto, int cantidad, Long proveedorId,
                                        String observacion, LocalDateTime fecha, int stockActual) {
    public static EntradaInventarioResponse from(MovimientoInventario m) {
        return new EntradaInventarioResponse(m.getId(), m.getProducto().getId(), m.getProducto().getNombre(),
                m.getCantidad(), m.getProveedor() == null ? null : m.getProveedor().getId(),
                m.getObservacion(), m.getFecha(), m.getProducto().getStock());
    }
}
