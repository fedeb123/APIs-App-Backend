package com.uade.tpo.petshop.entity;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data; 
@Data
@Builder
public class Pedido {
    private UUID id;
    private Cliente cliente;
    // private List<Producto> productos; Revisar
    private Date fechaPedido;
    private Estado estado; // PENDIENTE, ENVIADO, ENTREGADO, CANCELADO
}
