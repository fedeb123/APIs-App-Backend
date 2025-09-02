package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.entity.exceptions.ProductoDuplicateException;

public interface IProductoService {
    public Page<Producto> getAllProductos(PageRequest pageable);
    public Optional<Producto> getProductoById(Long id);
    public Producto createProducto(ProductoDTO producto) throws MissingCategoriaException, ProductoDuplicateException;
}
