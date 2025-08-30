package com.uade.tpo.petshop.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Rol;
import com.uade.tpo.petshop.entity.dtos.RolDTO;
import com.uade.tpo.petshop.entity.exceptions.RolDuplicateException;
import com.uade.tpo.petshop.service.RolService;

/*
 * Se utiliza Optional<> porque puede ser que no se devuelva nada (objeto vacio)
 * Se utiliza Page<> para representar una porcion una lista completa de resultados
 * @fedeb123
 */

@RestController
@RequestMapping("roles")
public class RolController {
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<Page<Rol>> getAllRoles(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        if (page == null && size == null){
            return ResponseEntity.ok(rolService.getAllRoles(PageRequest.of(0, Integer.MAX_VALUE)));
        } else {
            return ResponseEntity.ok(rolService.getAllRoles(PageRequest.of(page, size)));
        }
    }

    @GetMapping("/{rolId}")
    public ResponseEntity<Rol> getRolById(@PathVariable Long rolId) {
        Optional<Rol> rol = rolService.getRolById(rolId);
        if (rol.isPresent()){
            return ResponseEntity.ok(rol.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> createRol(@RequestBody RolDTO rol)
            throws RolDuplicateException {
        Rol nuevoRol = rolService.createRol(rol);
        return ResponseEntity.created(URI.create("/roles/" + nuevoRol.getId())).body(nuevoRol);
    }    
}
