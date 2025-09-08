package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * Esto atajaria cualquier excepcion generica 
 * no especificada
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepcionGenerica(Exception error){
        return ResponseEntity.status(500).body("Error Interno General: " + error);
    }
    
}
