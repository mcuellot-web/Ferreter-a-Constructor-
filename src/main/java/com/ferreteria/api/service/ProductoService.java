package com.ferreteria.api.service;

import com.ferreteria.api.dto.ProductoRequest;
import com.ferreteria.api.dto.ProductoResponse;
import java.util.List;

public interface ProductoService extends CrudService<ProductoRequest, ProductoResponse> {
    List<ProductoResponse> listarStockBajo();
}
