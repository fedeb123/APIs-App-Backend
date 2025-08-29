package com.uade.tpo.petshop.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class DetallePedido {
    public DetallePedido() {
    }

    public DetallePedido(Pedido pedido, Producto producto, int cantidad, double precioUnitario) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="pedido_id", nullable=false)
    private Pedido pedido;
    
    @Column
    private int cantidad;
    
    @Column
    private double precioUnitario;

    @ManyToOne()
    @JoinColumn(name="producto_id", nullable=false)
    private Producto producto;
}
