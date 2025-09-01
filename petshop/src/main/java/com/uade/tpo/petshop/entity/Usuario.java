package com.uade.tpo.petshop.entity;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity

public class Usuario {
    public Usuario() {
    }

    public Usuario(String nombre, String apellido,String telefono, String email, String direccion) {
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

    @OneToMany(mappedBy="usuario_creador")
    private List<Producto> productos_creados;
    
    @OneToMany(mappedBy="cliente")
    List<Pedido> pedidos;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
}
