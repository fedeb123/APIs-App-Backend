package com.uade.tpo.petshop.entity;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;

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

    public DetallePedido(Pedido pedido, Producto producto, int cantidad) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioSubtotal = producto.getPrecio() * cantidad;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private int cantidad;
    
    @Column
    private double precioSubtotal;

    @ManyToOne()
    @JoinColumn(name="producto_id", nullable=false)
    private Producto producto;

    @ManyToOne()
    @JoinColumn(name="pedido_id", nullable=false)
    private Pedido pedido;

    public DetallePedidoDTO toDTO(){
        return new DetallePedidoDTO(this.id, this.cantidad, this.precioSubtotal, this.producto.getId(), this.pedido.getId());
    }
}
