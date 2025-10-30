package com.uade.tpo.petshop.entity.dtos;

import com.uade.tpo.petshop.entity.enums.RolEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class RolDTO {
    private long id;
    
    @Enumerated(EnumType.STRING)
    private RolEnum nombre;

    public RolDTO(Long id, RolEnum nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public RolDTO() {
    }
}
