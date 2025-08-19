package com.uade.tpo.petshop.entity;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Categoria {
    private UUID id;
    private String nombreCategoria;
    private String descripcion;
}

