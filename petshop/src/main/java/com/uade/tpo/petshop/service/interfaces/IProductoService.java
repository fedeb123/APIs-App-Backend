package com.uade.tpo.petshop.service.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.Producto;

public interface IProductoService {
    public List<Producto> getAllProductos();
    public Optional<Producto> getProductoById(UUID id);
    public Producto createProducto(ProductoDTO producto);

}
