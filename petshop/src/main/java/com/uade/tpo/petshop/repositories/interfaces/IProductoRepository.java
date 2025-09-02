package com.uade.tpo.petshop.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    
    @Query(value="SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre%")
    List<Producto> findByNombre(String nombre);
    
    @Query(value="SELECT p FROM Producto p WHERE p.id = :id")
    Producto findProductoById(Long id);
}