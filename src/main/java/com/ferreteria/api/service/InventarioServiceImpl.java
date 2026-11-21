package com.ferreteria.api.service;

import com.ferreteria.api.dto.EntradaInventarioRequest;
import com.ferreteria.api.dto.EntradaInventarioResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.model.MovimientoInventario;
import com.ferreteria.api.model.Producto;
import com.ferreteria.api.model.Proveedor;
import com.ferreteria.api.repository.MovimientoInventarioRepository;
import com.ferreteria.api.repository.ProductoRepository;
import com.ferreteria.api.repository.ProveedorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public EntradaInventarioResponse registrarEntrada(EntradaInventarioRequest req) {
        Producto producto = productoRepository.findByIdForUpdate(req.productoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", req.productoId()));
        Proveedor proveedor = req.proveedorId() == null ? null
                : proveedorRepository.findById(req.proveedorId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", req.proveedorId()));
        producto.aumentarStock(req.cantidad());

        MovimientoInventario mov = new MovimientoInventario();
        mov.setProducto(producto);
        mov.setProveedor(proveedor);
        mov.setCantidad(req.cantidad());
        mov.setObservacion(req.observacion());
        return EntradaInventarioResponse.from(movimientoRepository.save(mov));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaInventarioResponse> historialPorProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new RecursoNoEncontradoException("Producto", productoId);
        }
        return movimientoRepository.findByProductoIdOrderByFechaDesc(productoId).stream()
                .map(EntradaInventarioResponse::from).toList();
    }
}
