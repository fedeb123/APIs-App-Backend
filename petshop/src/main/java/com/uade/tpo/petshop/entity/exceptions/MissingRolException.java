package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason="Rol Inexistente")
public class MissingRolException extends Exception {
    public MissingRolException() {
        super("Rol no encontrado");
    }

    public MissingRolException(String message) {
        super(message);
    }

    public MissingRolException(String message, Throwable cause) {
        super(message, cause);
    }
}
