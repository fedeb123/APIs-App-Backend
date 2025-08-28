package com.uade.tpo.petshop.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.Optional;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.service.productoService;


@RestController
@RequestMapping("productos")
public class productoController {
    // TODO : Hacer los controllers :)
    @Autowired
    private productoService productoService;

    @GetMapping
    public ResponseEntity<Page<Producto>> getAllProductos() {
        Page<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> getProductoById(@PathVariable UUID productoId) {
        Optional<Producto> producto = productoService.getProductoById(productoId);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    

}