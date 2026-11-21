package com.ferreteria.api.service;

import com.ferreteria.api.dto.ProductoRequest;
import com.ferreteria.api.dto.ProductoResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Categoria;
import com.ferreteria.api.model.Producto;
import com.ferreteria.api.model.Proveedor;
import com.ferreteria.api.repository.CategoriaRepository;
import com.ferreteria.api.repository.ProductoRepository;
import com.ferreteria.api.repository.ProveedorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest req) {
        if (productoRepository.existsBySku(req.sku())) {
            throw new ReglaNegocioException("Ya existe un producto con SKU " + req.sku());
        }
        Producto p = new Producto();
        p.setStock(req.stockInicial());
        return ProductoResponse.from(productoRepository.save(aplicar(p, req)));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest req) {
        Producto p = buscar(id);
        if (!p.getSku().equals(req.sku()) && productoRepository.existsBySku(req.sku())) {
            throw new ReglaNegocioException("Ya existe un producto con SKU " + req.sku());
        }
        return ProductoResponse.from(productoRepository.save(aplicar(p, req)));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return ProductoResponse.from(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepository.findByActivoTrue().stream().map(ProductoResponse::from).toList();
    }

    /** Baja lógica: se conserva el historial de ventas y movimientos. */
    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto p = buscar(id);
        p.setActivo(false);
        productoRepository.save(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarStockBajo() {
        return productoRepository.findConStockBajo().stream().map(ProductoResponse::from).toList();
    }

    private Producto aplicar(Producto p, ProductoRequest req) {
        if (req.precioVenta().compareTo(req.precioCosto()) < 0) {
            throw new ReglaNegocioException("El precio de venta no puede ser menor al precio de costo");
        }
        Categoria categoria = categoriaRepository.findById(req.categoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", req.categoriaId()));
        Proveedor proveedor = req.proveedorId() == null ? null
                : proveedorRepository.findById(req.proveedorId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", req.proveedorId()));
        p.setSku(req.sku());
        p.setNombre(req.nombre());
        p.setDescripcion(req.descripcion());
        p.setPrecioCosto(req.precioCosto());
        p.setPrecioVenta(req.precioVenta());
        p.setStockMinimo(req.stockMinimo());
        p.setCategoria(categoria);
        p.setProveedor(proveedor);
        return p;
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }
}
