package com.uade.tpo.petshop.entity;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class Producto {

    private UUID id;

    private String nombre;

    private String descripcion;

    private double precio;

    private int stock;

    private Categoria categoria;
}
