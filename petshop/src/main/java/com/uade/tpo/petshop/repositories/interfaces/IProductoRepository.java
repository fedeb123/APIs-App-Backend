package com.uade.tpo.petshop.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    
    @Query(value="SELECT p FROM Producto p WHERE p.nombre LIKE =?1")
    List<Producto> findByName(String nombre);

}