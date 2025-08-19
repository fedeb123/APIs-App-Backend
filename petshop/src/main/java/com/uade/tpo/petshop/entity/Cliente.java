package com.uade.tpo.petshop.entity;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Cliente {
    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;

    
}
