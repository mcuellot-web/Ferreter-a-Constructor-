package com.ferreteria.api.service;

import com.ferreteria.api.dto.CategoriaRequest;
import com.ferreteria.api.dto.CategoriaResponse;
import com.ferreteria.api.exception.RecursoNoEncontradoException;
import com.ferreteria.api.exception.ReglaNegocioException;
import com.ferreteria.api.model.Categoria;
import com.ferreteria.api.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CrudService<CategoriaRequest, CategoriaResponse> {

    private final CategoriaRepository repository;

    @Override
    @Transactional
    public CategoriaResponse crear(CategoriaRequest req) {
        if (repository.existsByNombreIgnoreCase(req.nombre())) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre '" + req.nombre() + "'");
        }
        Categoria c = new Categoria();
        c.setNombre(req.nombre());
        c.setDescripcion(req.descripcion());
        return CategoriaResponse.from(repository.save(c));
    }

    @Override
    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest req) {
        Categoria c = buscar(id);
        c.setNombre(req.nombre());
        c.setDescripcion(req.descripcion());
        return CategoriaResponse.from(repository.save(c));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtener(Long id) {
        return CategoriaResponse.from(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return repository.findAll().stream().map(CategoriaResponse::from).toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Categoria buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }
}
