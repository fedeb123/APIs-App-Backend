package com.uade.tpo.petshop.entity.dtos;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PedidoDTO {

    private Long id;
    UsuarioDTO cliente;
    private Date fecha;
    private EstadoDTO estado;
    private double precioTotal;
    private List<DetallePedidoDTO> detalles;
    private FacturaDTO factura;

    public PedidoDTO(Long id, UsuarioDTO cliente, Date fecha, EstadoDTO estado, double precioTotal, List<DetallePedidoDTO> detalles, FacturaDTO factura) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.precioTotal = precioTotal;
        this.detalles = detalles;
        this.factura = factura;
    }

    public PedidoDTO(){

    }
    
}
