package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Para cuando no se encuentra el detalle pedido
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DetallePedido no encontrado")
public class NotFoundException {
    
}
