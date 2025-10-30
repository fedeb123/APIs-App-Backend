package com.uade.tpo.petshop.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.petshop.entity.dtos.AuthResponseDTO;
import com.uade.tpo.petshop.entity.dtos.LoginRequestDTO;
import com.uade.tpo.petshop.entity.dtos.RegistroRequestDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.service.interfaces.IAutenticacionService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AutenticacionController {

    private final IAutenticacionService service;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegistroRequestDTO request)
            throws MissingRolException, UsuarioDuplicateException {
        return ResponseEntity.ok(service.registrar(request));
    }

    @PostMapping(value = "/authenticate", consumes = "application/json")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(service.autenticar(request));
    }
}

