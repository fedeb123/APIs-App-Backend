package com.uade.tpo.petshop.entity.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private Long categoriaId;
    private Long usuarioId;
    private String imageUrl;
    private boolean activo;
    private Date fechaBaja;

    public ProductoDTO(Long id, String nombre, String descripcion, double precio, int stock, Long categoriaId, Long usuadioId, String imageUrl, boolean activo, Date fechaBaja) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.usuarioId = usuadioId;
        this.imageUrl = imageUrl;
        this.activo = activo;
        this.fechaBaja = fechaBaja;
    }

    public ProductoDTO(String nombre){
        this.nombre = nombre;
    }

    public ProductoDTO(int stock){
        this.stock = stock;
    }

    public ProductoDTO() {
    }
}
