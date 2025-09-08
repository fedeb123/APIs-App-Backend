package com.uade.tpo.petshop.service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.service.interfaces.IJwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

@Service
public class JwtService implements IJwtService {
    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration:86400000}")
    private Long expiracionPorDefecto;

    private SecretKey getJwtKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean esValido(String token, UserDetails userDetails) {
        final String username = extraerUsuario(token);
        return (username.equals(userDetails.getUsername()) && !estaExpirado(token));
    }

    @Override
    public String generarToken(UserDetails userDetails){
        return construirToken(userDetails, expiracionPorDefecto);
    }

    @Override
    public boolean estaExpirado(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public String extraerUsuario(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
            .verifyWith(getJwtKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claimsResolver.apply(claims);
    }

    @Override
    public String construirToken(UserDetails userDetails, long expiracionCustomMs) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiracionCustomMs);

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
