package com.ferreteria.api.service;

import com.ferreteria.api.dto.EntradaInventarioRequest;
import com.ferreteria.api.dto.EntradaInventarioResponse;
import java.util.List;

public interface InventarioService {
    EntradaInventarioResponse registrarEntrada(EntradaInventarioRequest request);

    List<EntradaInventarioResponse> historialPorProducto(Long productoId);
}
