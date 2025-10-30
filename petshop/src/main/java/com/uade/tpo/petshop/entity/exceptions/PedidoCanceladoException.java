package com.uade.tpo.petshop.entity.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason="El Pedido fue cancelado")
public class PedidoCanceladoException extends Exception{
    
}
