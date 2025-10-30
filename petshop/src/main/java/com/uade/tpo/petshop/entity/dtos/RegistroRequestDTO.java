package com.uade.tpo.petshop.entity.dtos;


import lombok.Data;

@Data
public class RegistroRequestDTO {

    private String nombre;

    private String apellido;

    private String telefono;

    private String email;

    private String password;

    private String direccion;

    private Long rolId;
}
