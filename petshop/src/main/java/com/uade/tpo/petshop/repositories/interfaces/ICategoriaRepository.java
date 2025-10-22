package com.uade.tpo.petshop.repositories.interfaces;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria,Long>{
    
    @Query(value="select c from Categoria c where c.descripcion =?1")
    List<Categoria> findByDescripcion(String descripcion);
    
    @Query(value="select c from Categoria c where c.nombreCategoria =?1")
    List<Categoria> findByNombreCategoria(String nombreCategoria);

    @Query(value = "SELECT * FROM categoria WHERE activo = 0", countQuery = "SELECT COUNT(*) FROM categoria WHERE activo = 0", nativeQuery = true)
    Page<Categoria> findDescontinuadas(Pageable pageable);

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value = "UPDATE categoria SET activo = 1, fecha_baja = NULL WHERE id = :id", nativeQuery = true)
    int reactivarById(@Param("id") Long id);
    
}
