package com.uade.tpo.petshop.entity.dtos;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.Usuario;
import lombok.Data;

@Data
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    private Categoria categoria;
    private Usuario usuarioCreador;

    public ProductoDTO(Long id, String nombre, String descripcion, double precio, int stock, Categoria categoria, Usuario usuarioCreador) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.usuarioCreador = usuarioCreador;
    }

}
