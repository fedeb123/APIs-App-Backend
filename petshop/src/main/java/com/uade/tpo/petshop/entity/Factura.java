package com.uade.tpo.petshop.entity;

import java.util.Date;

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
public class Factura {

    public Factura() {
    }

    public Factura(Pedido pedido, Date fechaEmision, double total, metodoDePago metodoPago) {
        this.pedido = pedido;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.metodoPago = metodoPago;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="pedido_id", nullable=false)
    private Pedido pedido;
    
    @Column
    private Date fechaEmision;
    
    @Column
    private double total;
    
    @Column
    private metodoDePago metodoPago;
}
