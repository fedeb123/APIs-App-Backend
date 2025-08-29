package com.uade.tpo.petshop.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uade.tpo.petshop.entity.Producto;

public interface productoService {
    // metodo que nos trae todas las productos
    public Page<Producto> getAllProductos();
    public Optional<Producto> getProductoById(UUID id);
    public Producto createProducto(Producto producto);

}
