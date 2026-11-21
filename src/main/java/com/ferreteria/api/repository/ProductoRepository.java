package com.ferreteria.api.repository;

import com.ferreteria.api.model.Producto;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsBySku(String sku);

    List<Producto> findByActivoTrue();

    @Query("select p from Producto p where p.activo = true and p.stock <= p.stockMinimo order by p.stock asc")
    List<Producto> findConStockBajo();

    /** Bloqueo pesimista para evitar ventas concurrentes que dejen el stock negativo. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.id = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Long id);
}
