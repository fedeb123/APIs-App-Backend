package com.uade.tpo.petshop.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class Cliente {
    public Cliente() {
    }

    public Cliente(String nombre, String apellido,String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;
    
    @Column
    private String apellido;
    
    @Column
    private String telefono;

    @Column
    private String email;
    
    
    
    @Column
    private String direccion;

    
}
