package com.ferreteria.api.service;

import com.ferreteria.api.dto.VentaRequest;
import com.ferreteria.api.dto.VentaResponse;
import java.util.List;

public interface VentaService {
    VentaResponse registrar(VentaRequest request);

    VentaResponse obtener(Long id);

    List<VentaResponse> listar();
}
