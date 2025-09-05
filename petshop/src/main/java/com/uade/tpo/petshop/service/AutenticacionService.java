package com.uade.tpo.petshop.service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

import com.uade.tpo.petshop.service.interfaces.IAutenticacionService;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.AutenticacionResponseDTO;
import com.uade.tpo.petshop.entity.dtos.RegistroRequestDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.repositories.interfaces.IUsuarioRepository;
import com.uade.tpo.petshop.service.interfaces.IJwtService;
import com.uade.tpo.petshop.service.interfaces.IRolService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AutenticacionService implements IAutenticacionService{
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private IUsuarioRepository usuarioRepository;
    private final IRolService rolService;

    public AutenticacionResponseDTO register(RegistroRequestDTO request) {
    var user = Usuario.builder()
        .nombre(request.getNombre())
        .apellido(request.getApellido())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .direccion(request.getDireccion())
        .telefono(request.getTelefono())
        .rol(rolService.getRolById(request.getRolId())
                .orElseThrow(() -> new MissingRolException()))
        .build();

    // guardo al usuario
    usuarioRepository.save(user);

    var jwtToken = jwtService.generarToken(user);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
}

    public AuthenticationResponse autenticar(AuthenticationRequest request) {

    }

}
