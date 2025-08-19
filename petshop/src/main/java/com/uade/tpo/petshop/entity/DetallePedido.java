package com.uade.tpo.petshop.entity;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetallePedido {
    private UUID id;
    private Pedido pedido;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    
}
