package com.uade.tpo.petshop.repositories.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Producto;


@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    
    @Query(value="SELECT p FROM Producto p WHERE p.nombre LIKE ?1")
    List<Producto> findByName(String nombre);

    Page<Producto> findByStockGreaterThan(int stock, Pageable pageable);

    @Query(value = "SELECT * FROM producto WHERE activo = 0", countQuery = "SELECT COUNT(*) FROM producto WHERE activo = 0", nativeQuery = true)
    Page<Producto> findDescontinuados(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM producto WHERE categoria_id = :categoriaId", nativeQuery = true)
    long countAllByCategoriaId(@Param("categoriaId") Long categoriaId);

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value = "UPDATE producto SET activo = 1, fecha_baja = NULL WHERE id = :id", nativeQuery = true)
    int reactivarById(@Param("id") Long id);
}