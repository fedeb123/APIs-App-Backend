package com.uade.tpo.petshop.entity.dtos;

import com.uade.tpo.petshop.entity.enums.RolEnum;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class RolDTO {

    public RolDTO(long id, RolEnum nombre) {
        this.id = id;
        this.nombre = nombre;
    } 
    
    private long id;
    private RolEnum nombre;
}
