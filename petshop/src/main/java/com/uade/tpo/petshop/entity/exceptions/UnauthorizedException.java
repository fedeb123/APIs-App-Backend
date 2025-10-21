package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Para cuando el usuario intenta modificar un detalle que no le pertenece
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Acceso no autorizado")
public class UnauthorizedException extends Exception{
    
}
