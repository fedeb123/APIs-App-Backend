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
    public AuthResponseDTO registrar(RegistroRequestDTO request)
            throws MissingRolException, UsuarioDuplicateException {

        var rol = rolService.getRolById(request.getRolId())
            .orElseThrow(() -> new MissingRolException("Rol no encontrado: id=" + request.getRolId()));

        var user = Usuario.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .direccion(request.getDireccion())
            .telefono(request.getTelefono())
            .rol(rol)
            .build();

        usuarioService.createUsuario(user.toDTO());

        var jwtToken = jwtService.generarToken(user);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .build();
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
