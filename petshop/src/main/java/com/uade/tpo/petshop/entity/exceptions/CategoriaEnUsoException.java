package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT, reason="Categoria en Uso por Productos Activos")
public class CategoriaEnUsoException extends Exception {
    
}

