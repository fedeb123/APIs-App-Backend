package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Pedido no encontrado")
public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException() {
        super("Pedido no encontrado");
    }

    public PedidoNotFoundException(String message) {
        super(message);
    }
}