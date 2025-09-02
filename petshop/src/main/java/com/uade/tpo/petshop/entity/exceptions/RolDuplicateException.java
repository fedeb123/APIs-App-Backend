package com.uade.tpo.petshop.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/*
 * Por cada creacion, se tendria que agregar una excepcion del tipo duplicado
 * @fedeb123
*/

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason="Rol Duplicado")
public class RolDuplicateException extends Exception {
    
}
