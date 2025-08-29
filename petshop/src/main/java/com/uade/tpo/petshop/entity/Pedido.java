package com.uade.tpo.petshop.entity;

import java.util.Date;
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
public class Pedido {
    public Pedido() {
    }

    public Pedido(Cliente cliente, Date fechaPedido, Estado estado) {
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="cliente_id", nullable=false)
    private Cliente cliente;

    @Column
    private Date fechaPedido;
    
    @Column
    private Estado estado; // PENDIENTE, ENVIADO, ENTREGADO, CANCELADO

    @OneToMany(mappedBy="pedido")
    List<DetallePedido> detalles;

    @OneToMany(mappedBy="pedido")
    List<Factura> facturas;
}
