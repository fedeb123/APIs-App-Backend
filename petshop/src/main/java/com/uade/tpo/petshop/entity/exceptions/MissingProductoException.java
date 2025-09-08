package com.uade.tpo.petshop.entity.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Producto Inexistente")
public class MissingProductoException extends Exception {
    public MissingProductoException() {
        super("Producto Inexistente");
    }

    public MissingProductoException(String message) {
        super(message);
    }
}
