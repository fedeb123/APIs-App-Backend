package com.uade.tpo.petshop.entity;

import java.util.Date;
import java.util.Objects;

import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.enums.MetodoDePagoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Factura {

    public Factura() {
    }

    public Factura(Pedido pedido, double total, MetodoDePagoEnum metodoPago) {
        this.pedido = pedido;
        this.fechaEmision = new Date();
        this.total = total;
        this.metodoPago = metodoPago;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Date fechaEmision;
    
    @Column
    private double total;
    
    @Column
    @Enumerated(EnumType.STRING)
    private MetodoDePagoEnum metodoPago; // EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA

    @OneToOne()
    @JoinColumn(name="pedido_id", nullable=false)
    private Pedido pedido;

    public FacturaDTO toDTO(){
        return new FacturaDTO(this.id, this.fechaEmision, this.total, this.pedido.getId(), this.metodoPago);
    }

    public void updateFromDTO(FacturaDTO factura, Pedido pedido){

        if (!Objects.equals(pedido.getId(), this.getPedido().getId())){
            this.setPedido(pedido);
        }

        if (factura.getTotal() != this.getTotal()){
            this.setTotal(factura.getTotal());
        }

        if (factura.getMetodoDePago() != this.getMetodoPago()){
            this.setMetodoPago(factura.getMetodoDePago());
        }


    }
}
