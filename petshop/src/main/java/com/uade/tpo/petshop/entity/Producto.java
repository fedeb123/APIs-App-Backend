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
public class Producto {

    public Producto() {
    }

    public Producto(String nombre, String descripcion, double precio, int stock, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, unique=true)
    private String nombre;


    @Column
    private String descripcion;

    @Column
    private double precio;

    @Column
    private int stock;

    @ManyToOne()
    @JoinColumn(name="categoria_id", nullable=false)
    private Categoria categoria;

    @OneToMany(mappedBy="producto")
    List<DetallePedido> detallePedidos;

}
