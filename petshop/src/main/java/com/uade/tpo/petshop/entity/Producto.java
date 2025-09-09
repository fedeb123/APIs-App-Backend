package com.uade.tpo.petshop.entity;

import java.util.List;

import com.uade.tpo.petshop.entity.dtos.ProductoDTO;

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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    @ManyToOne()
    @JoinColumn(name="usuario_id",nullable=false)
    Usuario usuario_creador;

    @OneToMany(mappedBy="producto")
    List<DetallePedido> detallePedidos;

    public ProductoDTO toDTO(){
        return new ProductoDTO(this.id, this.nombre, this.descripcion, this.precio, this.stock, this.categoria.toDTO(), this.usuario_creador.toDTO());
    }

    public void updateFromDTO(ProductoDTO producto) {
        if (producto.getNombre() != null && !producto.getNombre().isEmpty()) {
            this.nombre = producto.getNombre();
        }
        if (producto.getDescripcion() != null && !producto.getDescripcion().isEmpty()) {
            this.descripcion = producto.getDescripcion();
        }
        if (producto.getPrecio() != this.getPrecio()) {
            this.precio = producto.getPrecio();
        }
        if (producto.getStock() != this.getPrecio()) {
            this.stock = producto.getStock();
        }
        if (producto.getCategoria() != null) {
            this.categoria = new Categoria(); // Asume entidad válida
            this.categoria.updateFromDTO(producto.getCategoria());
        }
        if (producto.getUsuarioCreador() != null) {
            this.usuario_creador = new Usuario();  // Asume entidad válida
            this.usuario_creador.updateFromDTO(producto.getUsuarioCreador());
        }
    }

}
