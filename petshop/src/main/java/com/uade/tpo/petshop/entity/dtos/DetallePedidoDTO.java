package com.uade.tpo.petshop.entity.dtos;

import lombok.Data;

@Data
public class DetallePedidoDTO {

	private Long id;
	private int cantidad;
	private double precioSubtotal;
	private Long productoId;
	private Long pedidoId;

	public DetallePedidoDTO(Long id, int cantidad, double precioSubtotal, Long productoId, Long pedidoId) {
		this.id = id;
		this.cantidad = cantidad;
		this.precioSubtotal = precioSubtotal;
		this.productoId = productoId;
		this.pedidoId = pedidoId;
	}

    public DetallePedidoDTO(){

    }
}
