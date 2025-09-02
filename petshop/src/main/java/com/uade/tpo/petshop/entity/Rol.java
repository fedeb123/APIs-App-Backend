package com.uade.tpo.petshop.entity;

import java.util.List;

import com.uade.tpo.petshop.entity.dtos.RolDTO;
import com.uade.tpo.petshop.entity.enums.RolEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    @Enumerated(EnumType.STRING)
    private RolEnum nombre;

    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;

    public RolDTO toDTO(){
        return new RolDTO(this.id, this.nombre);
    }
    
}

