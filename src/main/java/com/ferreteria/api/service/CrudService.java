package com.ferreteria.api.service;

import java.util.List;

/** Contrato CRUD genérico (ISP/DIP): los controladores dependen de esta abstracción. */
public interface CrudService<REQ, RES> {
    RES crear(REQ request);

    RES actualizar(Long id, REQ request);

    RES obtener(Long id);

    List<RES> listar();

    void eliminar(Long id);
}
