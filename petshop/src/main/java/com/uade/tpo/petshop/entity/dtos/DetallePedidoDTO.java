package com.uade.tpo.petshop.entity.dtos;

import lombok.Data;

@Data
public class DetallePedidoDTO {

	private Long id;
	private int cantidad;
	private double precioSubtotal;
	private ProductoDTO producto;
	private PedidoDTO pedido;

	public DetallePedidoDTO(Long id, int cantidad, double precioSubtotal, ProductoDTO producto, PedidoDTO pedido) {
		this.id = id;
		this.cantidad = cantidad;
		this.precioSubtotal = precioSubtotal;
		this.producto = producto;
		this.pedido = pedido;
	}

    public DetallePedidoDTO(){

    }
}
