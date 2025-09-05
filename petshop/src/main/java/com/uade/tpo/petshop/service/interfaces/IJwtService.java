package com.uade.tpo.petshop.service.interfaces;

import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface IJwtService {
    String extraerUsuario(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    boolean esValido(String token, UserDetails UserDetails);
    boolean estaExpirado(String token);
    String generarToken(UserDetails UserDetails);
    String generarToken(UserDetails userDetails, long expiracionCustomMs); // esto es por si nosotros le queremos meter el tiempo de expiracion del token
}
