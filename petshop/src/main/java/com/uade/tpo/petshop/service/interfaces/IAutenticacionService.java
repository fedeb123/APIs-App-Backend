package com.uade.tpo.petshop.service.interfaces;


import com.uade.tpo.petshop.entity.dtos.AuthResponseDTO;
import com.uade.tpo.petshop.entity.dtos.RegistroRequestDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.entity.dtos.LoginRequestDTO;


public interface IAutenticacionService {
    AuthResponseDTO registrar(RegistroRequestDTO request)
    throws MissingRolException, UsuarioDuplicateException;    
    
    AuthResponseDTO autenticar(LoginRequestDTO request);
}
