package com.uade.tpo.petshop.entity.dto;

import java.util.List;

import com.uade.tpo.petshop.entity.Producto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class CategoriaDTO {
    
    private Long id;
    
    private String nombreCategoria;
    
    private String descripcion;

    List<ProductoDTO> productos;
    public CategoriaDTO(Long id, String nombreCategoria, String descripcion) {
        this.id=id;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
    }
    
    

}

