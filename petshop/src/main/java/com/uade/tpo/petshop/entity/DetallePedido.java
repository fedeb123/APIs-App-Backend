package com.uade.tpo.petshop.entity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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

        //  Snapshot al momento de agregar al pedido
        this.nombreProducto = producto.getNombre();
        this.precioUnitario = producto.getPrecio();

        // Subtotal calculado con el precio snapshot
        this.precioSubtotal = this.precioUnitario * cantidad;
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
    @NotFound(action = NotFoundAction.IGNORE) // Si el Producto está inactivo y el filtro lo oculta, evita excepción
    private Producto producto;

    @ManyToOne()
    @JoinColumn(name="pedido_id", nullable=false)
    private Pedido pedido;

    // Campos snapshot
    @Column
    private String nombreProducto;

    @Column
    private double precioUnitario;

    public DetallePedidoDTO toDTO(){
        Long productoId = (producto != null ? producto.getId() : null);
        // En primer lugar usamos Producto; si no está, intentamos levantar del snapshot
        String nombreProd = (this.nombreProducto != null ? this.nombreProducto : (producto != null ? producto.getNombre() : null));
        return new DetallePedidoDTO(this.id, this.cantidad, this.precioSubtotal, productoId, this.pedido.getId(), nombreProd);
    }
}
