package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.Producto;

public interface IProductoService {
    public Page<Producto> getAllProductos(PageRequest pageable);
    public Optional<Producto> getProductoById(UUID id);
    public Producto createProducto(ProductoDTO producto);

}
