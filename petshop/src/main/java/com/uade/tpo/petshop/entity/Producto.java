package com.uade.tpo.petshop.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

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
//SQl Delete intercepta la instruccion de delete del entity manager del JPA y reemplaza el delete
@SQLDelete(sql = "UPDATE producto SET activo = false, fecha_baja = NOW() WHERE id = ?")
//SQL Restriction tambien intercepta instrucciones del JPA y les agrega una condicion al where
@org.hibernate.annotations.SQLRestriction("activo = true")
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
        this.activo = true;    
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

    @Column
    private String imageUrl;

    @Column
    private boolean activo;
    
    @Column
    private Date fechaBaja;

    public ProductoDTO toDTO(){
        return new ProductoDTO(this.id, this.nombre, this.descripcion, this.precio, this.stock, this.categoria != null ? this.categoria.getId():null, this.usuario_creador != null ? this.usuario_creador.getId():null, this.imageUrl, this.activo, this.fechaBaja);
    }

    public void updateFromDTO(ProductoDTO producto, Categoria categoria, Usuario usuario) {
        if (producto.getNombre() != null && !producto.getNombre().isEmpty()) {
            this.nombre = producto.getNombre();
        }
        if (producto.getDescripcion() != null && !producto.getDescripcion().isEmpty()) {
            this.descripcion = producto.getDescripcion();
        }
        if (producto.getPrecio() != this.precio) {
            this.precio = producto.getPrecio();
        }
        if (producto.getStock() != this.stock) {
            this.stock = producto.getStock();
        }
        if (categoria != null) {
            this.categoria = categoria;
        }
        if (usuario != null) {
            this.usuario_creador = usuario;
        }
        if (producto.getImageUrl() != null){
            this.imageUrl = producto.getImageUrl();
        }

        this.activo = producto.isActivo();
        
        if (producto.getFechaBaja() != null) {
            this.fechaBaja = producto.getFechaBaja();
        }

    }

}
