package com.uade.tpo.petshop.entity.dtos;

import com.uade.tpo.petshop.entity.enums.RolEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class RolDTO {

    public RolDTO(long id, RolEnum nombre) {
        this.id = id;
        this.nombre = nombre;
    } 
    
    private long id;
    @Enumerated(EnumType.STRING)
    private RolEnum nombre;
}
