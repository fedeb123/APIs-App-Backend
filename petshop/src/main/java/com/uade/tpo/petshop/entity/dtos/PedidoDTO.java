package com.uade.tpo.petshop.entity.dtos;

import java.util.Date;
import java.util.List;

import com.uade.tpo.petshop.entity.enums.EstadoEnum;

import lombok.Data;

@Data
public class PedidoDTO {

    private Long id;
    UsuarioDTO cliente;
    private Date fecha;
    private EstadoEnum estado;
    private double precioTotal;
    private List<DetallePedidoDTO> detalles;
    private FacturaDTO factura;

    public PedidoDTO(Long id, UsuarioDTO cliente, Date fecha, EstadoEnum estado, double precioTotal, List<DetallePedidoDTO> detalles, FacturaDTO factura) {
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
