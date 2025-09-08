package com.uade.tpo.petshop.service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

import com.uade.tpo.petshop.service.interfaces.IAutenticacionService;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.AuthResponseDTO;
import com.uade.tpo.petshop.entity.dtos.RegistroRequestDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.service.interfaces.IJwtService;
import com.uade.tpo.petshop.service.interfaces.IRolService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.uade.tpo.petshop.entity.dtos.LoginRequestDTO;

@Service
@RequiredArgsConstructor
public class AutenticacionService implements IAutenticacionService {

    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IRolService rolService;
    private final IUsuarioService usuarioService;

    @Override
    @Transactional
    public AuthResponseDTO registrar(RegistroRequestDTO request) {
        try {
            var user = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .rol(rolService.getRolById(request.getRolId())
                        .orElseThrow(MissingRolException::new))
                .build();

            usuarioService.createUsuario(user.toDTO());

            var jwtToken = jwtService.generarToken(user); // si tu JwtService acepta UserDetails/Usuario
            return AuthResponseDTO.builder()
                    .accessToken(jwtToken)
                    .build();
        } catch (MissingRolException | UsuarioDuplicateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public AuthResponseDTO autenticar(LoginRequestDTO request) {
        var authentication = authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var jwtToken = jwtService.generarToken(userDetails);

        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .build();
    }
}
