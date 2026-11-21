package com.ferreteria.api.service;

import com.ferreteria.api.dto.ItemVentaRequest;
import com.ferreteria.api.dto.VentaRequest;
import com.ferreteria.api.dto.VentaResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Cliente;
import com.ferreteria.api.model.DetalleVenta;
import com.ferreteria.api.model.Producto;
import com.ferreteria.api.model.Venta;
import com.ferreteria.api.repository.ClienteRepository;
import com.ferreteria.api.repository.ProductoRepository;
import com.ferreteria.api.repository.VentaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest req) {
        Venta venta = new Venta();
        if (req.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(req.clienteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", req.clienteId()));
            venta.setCliente(cliente);
        }
        for (ItemVentaRequest item : req.items()) {
            Producto producto = productoRepository.findByIdForUpdate(item.productoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto", item.productoId()));
            if (!producto.isActivo()) {
                throw new ReglaNegocioException("El producto '" + producto.getNombre() + "' está inactivo");
            }
            producto.descontarStock(item.cantidad());
            venta.agregarDetalle(new DetalleVenta(producto, item.cantidad(), producto.getPrecioVenta()));
        }
        venta.calcularTotal();
        return VentaResponse.from(ventaRepository.save(venta));
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtener(Long id) {
        return VentaResponse.from(ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        return ventaRepository.findAll().stream().map(VentaResponse::from).toList();
    }
}
