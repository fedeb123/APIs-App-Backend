package com.uade.tpo.petshop.entity.dtos;

import java.util.Date;

import com.uade.tpo.petshop.entity.enums.MetodoDePagoEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class FacturaDTO {
    
    private Long id;
    private Date fechaEmision;
    private double total;
    private PedidoDTO pedido;
    @Enumerated(EnumType.STRING)
    private MetodoDePagoEnum metodoDePago;    
    
    public FacturaDTO(){

    }

    public FacturaDTO(Long id, Date fechaEmision, double total, PedidoDTO pedido, MetodoDePagoEnum metodoDePago){
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.pedido = pedido;
        this.metodoDePago = metodoDePago;
    }
}
