package com.ferreteria.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ferreteria.api.dto.ProductoRequest;
import com.ferreteria.api.dto.ProductoResponse;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Categoria;
import com.ferreteria.api.model.Producto;
import com.ferreteria.api.repository.CategoriaRepository;
import com.ferreteria.api.repository.ProductoRepository;
import com.ferreteria.api.repository.ProveedorRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock ProductoRepository productoRepository;
    @Mock CategoriaRepository categoriaRepository;
    @Mock ProveedorRepository proveedorRepository;
    @InjectMocks ProductoServiceImpl service;

    private ProductoRequest request(String precioCosto, String precioVenta) {
        return new ProductoRequest("TOR-010", "Tornillo 1/2", null, new BigDecimal(precioCosto),
                new BigDecimal(precioVenta), 100, 20, 1L, null);
    }

    private Categoria categoria() {
        Categoria c = new Categoria();
        c.setId(1L);
        c.setNombre("Tornillería");
        return c;
    }

    @Test
    void crear_guardaProductoConStockInicial() {
        when(productoRepository.existsBySku("TOR-010")).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria()));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoResponse res = service.crear(request("100", "150"));

        assertEquals("TOR-010", res.sku());
        assertEquals(100, res.stock());
        assertEquals("Tornillería", res.categoria());
    }

    @Test
    void crear_conSkuDuplicado_lanzaReglaNegocio() {
        when(productoRepository.existsBySku("TOR-010")).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> service.crear(request("100", "150")));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void crear_conPrecioVentaMenorAlCosto_lanzaReglaNegocio() {
        when(productoRepository.existsBySku("TOR-010")).thenReturn(false);

        assertThrows(ReglaNegocioException.class, () -> service.crear(request("200", "150")));
    }

    @Test
    void eliminar_hacebajaLogica() {
        Producto p = new Producto();
        p.setId(5L);
        when(productoRepository.findById(5L)).thenReturn(Optional.of(p));

        service.eliminar(5L);

        assertFalse(p.isActivo());
        verify(productoRepository).save(p);
    }

    @Test
    void listarStockBajo_devuelveProductosBajoElMinimo() {
        Producto p = new Producto();
        p.setId(7L);
        p.setSku("CLA-001");
        p.setNombre("Clavos");
        p.setPrecioCosto(BigDecimal.ONE);
        p.setPrecioVenta(BigDecimal.TEN);
        p.setStock(2);
        p.setStockMinimo(5);
        p.setCategoria(categoria());
        when(productoRepository.findConStockBajo()).thenReturn(List.of(p));

        List<ProductoResponse> res = service.listarStockBajo();

        assertEquals(1, res.size());
        assertEquals("CLA-001", res.get(0).sku());
    }
}
