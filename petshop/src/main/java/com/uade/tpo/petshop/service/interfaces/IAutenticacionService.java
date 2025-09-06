package com.uade.tpo.petshop.service.interfaces;


import com.uade.tpo.petshop.entity.dtos.AuthResponseDTO;
import com.uade.tpo.petshop.entity.dtos.RegistroRequestDTO;


public interface IAutenticacionService {
    AuthResponseDTO registrar(RegistroRequestDTO request);
    AuthResponseDTO autenticar(String email, String password);
}
