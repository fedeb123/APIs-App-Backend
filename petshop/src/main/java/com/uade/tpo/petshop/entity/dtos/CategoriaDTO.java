package com.uade.tpo.petshop.entity.dtos;

import java.util.List;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombreCategoria;
    private String descripcion;
    private List<ProductoDTO> productos;

    public CategoriaDTO(Long id, String nombreCategoria, String descripcion, List<ProductoDTO> productos) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.productos = productos;
    }

    public CategoriaDTO() {
    }
}

