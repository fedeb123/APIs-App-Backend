package com.uade.tpo.petshop.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.petshop.entity.Producto;


@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    
    @Query(value="SELECT p FROM Producto p WHERE p.nombre LIKE ?1")
    List<Producto> findByName(String nombre);

    Page<Producto> findByStockGreaterThan(int stock, Pageable pageable);

}