package com.uade.tpo.petshop.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import com.uade.tpo.petshop.entity.dtos.AutenticacionResponseDTO;
import com.uade.tpo.petshop.entity.dtos.RegistroRequestDTO;


public interface IAutenticacionService {
    AutenticacionResponseDTO register(RegistroRequestDTO request);
    AutenticacionResponseDTO autenticar(String email, String password);
}
