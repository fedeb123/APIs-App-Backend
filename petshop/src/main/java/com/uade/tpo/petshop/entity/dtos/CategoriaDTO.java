package com.uade.tpo.petshop.entity.dtos;

import java.util.List;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombreCategoria;
    private String descripcion;


    public CategoriaDTO(Long id, String nombreCategoria, String descripcion) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;

    }

    public CategoriaDTO() {
    }
}

