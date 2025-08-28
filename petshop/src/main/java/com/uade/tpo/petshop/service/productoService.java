package com.uade.tpo.petshop.service;

import com.uade.tpo.petshop.entity.Producto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.uade.tpo.petshop.entity.Categoria;
import org.springframework.data.domain.Page;

public interface productoService {
    // metodo que nos trae todas las productos
    public Page<Producto> getAllProductos();
    public Optional<Producto> getProductoById(UUID id);
    public Producto createProducto(Producto producto);
}
