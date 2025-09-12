package com.uade.tpo.petshop.entity.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason="Factura Inexistente")
public class MissingFacturaException extends Exception{
    
}
