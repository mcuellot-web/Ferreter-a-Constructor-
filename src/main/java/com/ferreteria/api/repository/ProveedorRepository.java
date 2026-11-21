package com.ferreteria.api.repository;

import com.ferreteria.api.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByNit(String nit);
}
