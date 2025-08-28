package com.uade.tpo.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Producto;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    @Query(value="SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre%")
    List<Producto> findByNombre(String nombre);
    
    @Query(value="SELECT p FROM Producto p WHERE p.id = :id")
    Producto findProductoById(UUID id);

    @Query(value="SELECT p FROM Producto p")
    Page<Producto> findAllProductos();

}