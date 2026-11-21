package com.ferreteria.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Producto;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void descontarStock_reduceLaCantidad() {
        Producto p = new Producto();
        p.setStock(10);
        p.descontarStock(4);
        assertEquals(6, p.getStock());
    }

    @Test
    void descontarStock_masDelDisponible_lanzaExcepcion() {
        Producto p = new Producto();
        p.setNombre("Taladro");
        p.setStock(2);
        assertThrows(ReglaNegocioException.class, () -> p.descontarStock(3));
    }

    @Test
    void aumentarStock_sumaLaCantidad() {
        Producto p = new Producto();
        p.setStock(2);
        p.aumentarStock(8);
        assertEquals(10, p.getStock());
    }

    @Test
    void tieneStockBajo_segunStockMinimo() {
        Producto p = new Producto();
        p.setStockMinimo(5);
        p.setStock(5);
        assertTrue(p.tieneStockBajo());
        p.setStock(6);
        assertFalse(p.tieneStockBajo());
    }
}
