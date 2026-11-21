package com.ferreteria.api.dto;

import com.ferreteria.api.model.DetalleVenta;
import com.ferreteria.api.model.Venta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VentaResponse(Long id, LocalDateTime fecha, Long clienteId, String cliente, BigDecimal total,
                            List<Detalle> items) {

    public record Detalle(Long productoId, String producto, int cantidad, BigDecimal precioUnitario,
                          BigDecimal subtotal) {
        static Detalle from(DetalleVenta d) {
            return new Detalle(d.getProducto().getId(), d.getProducto().getNombre(), d.getCantidad(),
                    d.getPrecioUnitario(), d.getSubtotal());
        }
    }

    public static VentaResponse from(Venta v) {
        return new VentaResponse(v.getId(), v.getFecha(),
                v.getCliente() == null ? null : v.getCliente().getId(),
                v.getCliente() == null ? null : v.getCliente().getNombre(),
                v.getTotal(), v.getDetalles().stream().map(Detalle::from).toList());
    }
}
