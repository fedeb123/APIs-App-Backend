package com.uade.tpo.petshop.entity;

import java.util.UUID;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Factura {
    private UUID id;
    private Pedido pedido;
    private Date fechaEmision;
    private double total;
    private metodoDePago metodoPago;
}
