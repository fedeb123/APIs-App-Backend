package com.uade.tpo.petshop.entity.dtos;

import java.util.Date;
import java.util.List;

import com.uade.tpo.petshop.entity.enums.EstadoEnum;

import lombok.Data;

@Data
public class PedidoDTO {

    private Long id;
    private Long clienteId; /*(para post sencillo) */
    private Date fechaPedido; /*Se asigna automaticamente */
    private EstadoEnum estado;
    private double precioTotal;
    private List<DetallePedidoDTO> detalles; /*(con producto ID y cantidad) */
    private FacturaDTO factura;

    public PedidoDTO(Long id, Long clienteId, Date fechaPedido, EstadoEnum estado, double precioTotal, List<DetallePedidoDTO> detalles, FacturaDTO factura) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.estado=estado;
        this.precioTotal = precioTotal;
        this.detalles = detalles;
        this.factura = factura;
    }

    public PedidoDTO(){

    }
    
}
