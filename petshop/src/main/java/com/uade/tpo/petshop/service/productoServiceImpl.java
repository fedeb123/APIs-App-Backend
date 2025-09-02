package com.uade.tpo.petshop.service;

import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.repository.ProductoRepository;
import java.util.Optional;
import com.uade.tpo.petshop.entity.Producto;
import org.springframework.data.domain.Page;
import java.util.UUID;
import java.util.List;
import com.uade.tpo.petshop.entity.Categoria;

@Service
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Page<Producto> getAllProductos() {
        return productoRepository.findAllProductos();
    }

    @Override
    public Optional<Producto> getProductoById(UUID id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto createProducto(String nombre, String description, double precio, int stock, Categoria categoria) {
        // Implementacion del metodo para crear un nuevo producto
        List<Producto> productos = productoRepository.findByNombre(nombre);
        if(productos.isEmpty()){
            return productoRepository.save(new Producto(UUID.randomUUID(), nombre, description, precio, stock, categoria));
        } else {
            return null;
        }
    }
}
