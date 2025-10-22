package com.uade.tpo.petshop.entity.dtos;


import java.util.Date;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombreCategoria;
    private String descripcion;
    private boolean activo;
    private Date fechaBaja;


    public CategoriaDTO(Long id, String nombreCategoria, String descripcion, boolean activo, Date fechaBaja) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.activo = activo;
        this.fechaBaja = fechaBaja;

    }

    public CategoriaDTO() {
    }
}

