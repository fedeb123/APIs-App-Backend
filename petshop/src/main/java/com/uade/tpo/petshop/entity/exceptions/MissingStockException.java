package com.uade.tpo.petshop.entity.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason="Stock insuficiente frente a cantidad requerida")
public class MissingStockException extends Exception {
    
}
