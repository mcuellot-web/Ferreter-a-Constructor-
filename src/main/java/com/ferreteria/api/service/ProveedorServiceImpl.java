package com.ferreteria.api.service;

import com.ferreteria.api.dto.ProveedorRequest;
import com.ferreteria.api.dto.ProveedorResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Proveedor;
import com.ferreteria.api.repository.ProveedorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements CrudService<ProveedorRequest, ProveedorResponse> {

    private final ProveedorRepository repository;

    @Override
    @Transactional
    public ProveedorResponse crear(ProveedorRequest req) {
        if (repository.existsByNit(req.nit())) {
            throw new ReglaNegocioException("Ya existe un proveedor con NIT " + req.nit());
        }
        return ProveedorResponse.from(repository.save(aplicar(new Proveedor(), req)));
    }

    @Override
    @Transactional
    public ProveedorResponse actualizar(Long id, ProveedorRequest req) {
        return ProveedorResponse.from(repository.save(aplicar(buscar(id), req)));
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponse obtener(Long id) {
        return ProveedorResponse.from(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponse> listar() {
        return repository.findAll().stream().map(ProveedorResponse::from).toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Proveedor aplicar(Proveedor p, ProveedorRequest req) {
        p.setNombre(req.nombre());
        p.setNit(req.nit());
        p.setTelefono(req.telefono());
        p.setEmail(req.email());
        return p;
    }

    private Proveedor buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", id));
    }
}
