package com.uade.tpo.petshop.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.enums.EstadoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data; 

@Data
@Entity
public class Pedido {
    public Pedido() {
    }

    public Pedido(Usuario cliente, Date fechaPedido, EstadoEnum estado) {
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.precioTotal = 0;
        this.detalles = new ArrayList<>();
        this.factura = null;
    }

    public Pedido(Usuario cliente, Date fechaPedido, EstadoEnum estado, float precioTotal,List<DetallePedido> detalles, Factura factura) {
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.precioTotal = precioTotal;
        this.detalles = detalles;
        this.factura = factura;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario cliente;

    @Column
    private Date fechaPedido;
    
    @Column
    @Enumerated(EnumType.STRING)
    private EstadoEnum estado; // PENDIENTE, ENVIADO, ENTREGADO, CANCELADO

    @Column
    private double precioTotal;

    @OneToMany(mappedBy="pedido")
    List<DetallePedido> detalles;

    @OneToOne(mappedBy="pedido")
    Factura factura;

    public PedidoDTO toDTO(){
        List<DetallePedidoDTO> detallesDTO = new ArrayList<>();
        if (this.detalles != null) {
            for (DetallePedido d : this.detalles) {
                if (d != null) {
                    detallesDTO.add(d.toDTO());
                }
            }
        }

        return new PedidoDTO(
            this.id,
            (this.cliente != null) ? this.cliente.getId() : null,
            this.fechaPedido,
            this.estado,
            this.precioTotal,
            detallesDTO,
            (this.factura != null) ? this.factura.toDTO() : null 
        );
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        DetallePedido detalle = new DetallePedido(this, producto, cantidad, producto.getPrecio() * cantidad);
        this.detalles.add(detalle);
        this.precioTotal += detalle.getPrecioSubtotal(); // Actualizar el precio total
    }

    public void agregarFactura(FacturaDTO facturaDTO) {
        this.factura = new Factura(this, facturaDTO.getFechaEmision(), facturaDTO.getTotal(), facturaDTO.getMetodoDePago());
    }

    //calcular precio total del pedido

}
