package com.uade.tpo.petshop.service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.uade.tpo.petshop.service.interfaces.IAutenticacionService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AutenticacionService implements IAutenticacionService{
    
    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration:86400000}")
    private Long expiracionPorDefecto;

    private SecretKey getJwtKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extraerUsuario(String token) {
        try {
            return Jwts.parser()
                .verifyWith(getJwtKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean esValido(String token, UserDetails userDetails) {
        final String username = extraerUsuario(token);
        if (username == null || !username.equals(userDetails.getUsername())) {
            return false;
        }
        try {
            var claims = Jwts.parser()
                .verifyWith(getJwtKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            Date exp = claims.getExpiration();
            return exp != null && exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generarToken(UserDetails userDetails){
        return generarToken(userDetails, expiracionPorDefecto);
    }

    @Override
    public String generarToken(UserDetails userDetails, long expiracionCustomMs) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiracionCustomMs);

        // Serializamos authorities como lista de strings (ROLE_*)
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(exp)
                .claims(Map.of("roles", roles))
                .signWith(getJwtKey(), Jwts.SIG.HS256)
                .compact();
    }
}
