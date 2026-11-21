package com.ferreteria.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ferreteria.api.dto.ItemVentaRequest;
import com.ferreteria.api.dto.VentaRequest;
import com.ferreteria.api.dto.VentaResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Producto;
import com.ferreteria.api.model.Venta;
import com.ferreteria.api.repository.ClienteRepository;
import com.ferreteria.api.repository.ProductoRepository;
import com.ferreteria.api.repository.VentaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock VentaRepository ventaRepository;
    @Mock ProductoRepository productoRepository;
    @Mock ClienteRepository clienteRepository;
    @InjectMocks VentaServiceImpl service;

    private Producto martillo;

    @BeforeEach
    void setUp() {
        martillo = new Producto();
        martillo.setId(1L);
        martillo.setSku("MAR-001");
        martillo.setNombre("Martillo");
        martillo.setPrecioCosto(new BigDecimal("15000"));
        martillo.setPrecioVenta(new BigDecimal("25000"));
        martillo.setStock(10);
    }

    @Test
    void registrarVenta_descuentaStockYCalculaTotal() {
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(martillo));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        VentaResponse res = service.registrar(new VentaRequest(null, List.of(new ItemVentaRequest(1L, 3))));

        assertEquals(7, martillo.getStock());
        assertEquals(0, new BigDecimal("75000").compareTo(res.total()));
        assertEquals(1, res.items().size());
    }

    @Test
    void registrarVenta_conStockInsuficiente_lanzaReglaNegocio() {
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(martillo));

        assertThrows(ReglaNegocioException.class,
                () -> service.registrar(new VentaRequest(null, List.of(new ItemVentaRequest(1L, 11)))));

        assertEquals(10, martillo.getStock());
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void registrarVenta_conProductoInactivo_lanzaReglaNegocio() {
        martillo.setActivo(false);
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(martillo));

        assertThrows(ReglaNegocioException.class,
                () -> service.registrar(new VentaRequest(null, List.of(new ItemVentaRequest(1L, 1)))));
    }

    @Test
    void registrarVenta_conProductoInexistente_lanzaNoEncontrado() {
        when(productoRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.registrar(new VentaRequest(null, List.of(new ItemVentaRequest(99L, 1)))));
    }
}
