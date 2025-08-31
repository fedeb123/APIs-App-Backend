package com.uade.tpo.petshop.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;


@Data
@Entity
public class Rol {

    public Rol(){

    }

    public Rol(String nombre){
        this.nombre=nombre;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID_ROL;

    @Column
    private String nombre;

    @OneToMany(mappedBy="rol")
    List<Usuario> usuarios;
}

