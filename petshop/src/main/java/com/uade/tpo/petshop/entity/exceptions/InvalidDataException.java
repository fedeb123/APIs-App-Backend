package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Para cuando los datos del detalle son inválidos (cantidad negativa, excede stock, etc.)
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Datos inválidos")
public class InvalidDataException {
    
}
