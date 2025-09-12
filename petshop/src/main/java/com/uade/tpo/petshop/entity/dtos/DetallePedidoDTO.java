package com.uade.tpo.petshop.entity.dtos;

import lombok.Data;

@Data
public class DetallePedidoDTO {

	private Long id;
	private int cantidad;
	private Long productoId;
	private Long pedidoId;
	private double precioSubtotal;

	public DetallePedidoDTO(int cantidad, Long productoId, Long pedidoId) {
        this.cantidad = cantidad;
        this.productoId = productoId;
        this.pedidoId = pedidoId;
    }

	public DetallePedidoDTO(Long id, int cantidad, double precioSubtotal, Long productoId, Long pedidoId) {
		this.id = id;
		this.cantidad = cantidad;
		this.productoId = productoId;
		this.pedidoId = pedidoId;
		this.precioSubtotal = precioSubtotal;
    }

    public DetallePedidoDTO(){

    }
}
