package com.uade.tpo.petshop.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.ProductoDuplicateException;

public interface IProductoService {
    public Page<Producto> getAllProductos(PageRequest pageable);
    public Optional<Producto> getProductoById(Long id);
    public List<Producto> getProductoByName(String name);
    public Producto createProducto(ProductoDTO producto) throws MissingCategoriaException, ProductoDuplicateException;
    public Producto updateProducto(Long id, ProductoDTO producto) throws MissingCategoriaException, MissingProductoException, ProductoDuplicateException, MissingUserException;
}
