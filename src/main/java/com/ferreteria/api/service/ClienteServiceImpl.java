package com.ferreteria.api.service;

import com.ferreteria.api.dto.ClienteRequest;
import com.ferreteria.api.dto.ClienteResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Cliente;
import com.ferreteria.api.repository.ClienteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements CrudService<ClienteRequest, ClienteResponse> {

    private final ClienteRepository repository;

    @Override
    @Transactional
    public ClienteResponse crear(ClienteRequest req) {
        if (repository.existsByDocumento(req.documento())) {
            throw new ReglaNegocioException("Ya existe un cliente con documento " + req.documento());
        }
        return ClienteResponse.from(repository.save(aplicar(new Cliente(), req)));
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest req) {
        return ClienteResponse.from(repository.save(aplicar(buscar(id), req)));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtener(Long id) {
        return ClienteResponse.from(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return repository.findAll().stream().map(ClienteResponse::from).toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Cliente aplicar(Cliente c, ClienteRequest req) {
        c.setNombre(req.nombre());
        c.setDocumento(req.documento());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        return c;
    }

    private Cliente buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }
}
