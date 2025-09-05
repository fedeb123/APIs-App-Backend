package com.uade.tpo.petshop.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;


public interface IAutenticacionService {
    String extraerUsuario(String token);
    boolean esValido(String token, UserDetails UserDetails);
    String generarToken(UserDetails UserDetails);
    String generarToken(UserDetails userDetails, long expiracionCustomMs); // esto es por si nosotros le queremos meter el tiempo de expiracion del token

}
