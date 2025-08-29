package com.uade.tpo.petshop.entity;

import com.uade.tpo.petshop.entity.dtos.RolDTO;
import com.uade.tpo.petshop.entity.enums.RolEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Rol {

    public Rol(RolEnum nombre) {
        this.nombre = nombre;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column
    private RolEnum nombre;

    public RolDTO toDTO(){
        return new RolDTO(this.id, this.nombre);
    }
    
}

