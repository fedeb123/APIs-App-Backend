package com.uade.tpo.petshop.service;

import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.repositories.interfaces.IProductoRepository;
import com.uade.tpo.petshop.service.interfaces.IProductoService;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Service
public class ProductoService implements IProductoService {
    private final IProductoRepository productoRepository;

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Page<Producto> getAllProductos(PageRequest pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto createProducto(ProductoDTO producto) {
        List<Producto> productos = productoRepository.findByName(producto.getNombre());
        if(productos.isEmpty()){
            return productoRepository.save(
                new Producto(
                    producto.getNombre(), 
                    producto.getDescripcion(), 
                    producto.getPrecio(), 
                    producto.getStock(), 
                    producto.getCategoria()));
        } else {
            return null;
        }
    }
}
