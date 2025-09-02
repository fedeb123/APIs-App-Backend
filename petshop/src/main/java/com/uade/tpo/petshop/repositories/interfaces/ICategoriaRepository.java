package com.uade.tpo.petshop.repositories.interfaces;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria,Long>{
    
    @Query(value="select c from Categoria c where c.descripcion =?1")
    List<Categoria> findByDescripcion(String descripcion);
    
    @Query(value="select c from Categoria c where c.nombreCategoria =?1")
    List<Categoria> findByNombreCategoria(String nombreCategoria);
    
}
