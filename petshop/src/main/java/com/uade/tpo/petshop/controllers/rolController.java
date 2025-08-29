package com.uade.tpo.petshop.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Rol;
import com.uade.tpo.petshop.service.RolService;



@RestController
@Controller
public class rolController {
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<Page<Rol>> getAllRoles() {
        Page<Rol> roles = rolService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{rolId}")
    public ResponseEntity<Rol> getRolById(@PathVariable UUID rolId) {
        Optional<Rol> rol = rolService.getRolById(rolId);
        return rol.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
}
