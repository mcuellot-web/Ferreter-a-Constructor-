package com.ferreteria.api.model;

import com.ferreteria.api.exception.ReglaNegocioException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Getter @Setter @NoArgsConstructor
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "precio_costo", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCosto;

    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private int stock;

    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo;

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    public void descontarStock(int cantidad) {
        if (cantidad > stock) {
            throw new ReglaNegocioException("Stock insuficiente para '" + nombre + "': disponible " + stock
                    + ", solicitado " + cantidad);
        }
        stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        stock += cantidad;
    }

    public boolean tieneStockBajo() {
        return stock <= stockMinimo;
    }
}
