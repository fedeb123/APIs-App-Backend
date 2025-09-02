package com.uade.tpo.petshop.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.service.interfaces.IProductoService;


@RestController
@RequestMapping("api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<Producto>> getAllProductos(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        if (page == null && size == null){
            return ResponseEntity.ok(productoService.getAllProductos(PageRequest.of(0, Integer.MAX_VALUE)));
        } else {
            return ResponseEntity.ok(productoService.getAllProductos(PageRequest.of(page, size)));
        }
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long productoId) {
        Optional<Producto> producto = productoService.getProductoById(productoId);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}