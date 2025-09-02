package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.entity.exceptions.ProductoDuplicateException;
import com.uade.tpo.petshop.repositories.interfaces.IProductoRepository;
import com.uade.tpo.petshop.service.interfaces.ICategoriaService;
import com.uade.tpo.petshop.service.interfaces.IProductoService;

import jakarta.transaction.Transactional;

@Service
public class ProductoService implements IProductoService {
    private final IProductoRepository productoRepository;
    private final ICategoriaService categoriaService;

    public ProductoService(IProductoRepository productoRepository, ICategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
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
    @Transactional
    public Producto createProducto(ProductoDTO producto) throws MissingCategoriaException, ProductoDuplicateException {
        // Implementacion del metodo para crear un nuevo producto
        List<Producto> productos = productoRepository.findByName(producto.getNombre());
        if(productos.isEmpty()){
            Optional<Categoria> categoriaProducto = categoriaService.getCategoriaByNombre(producto.getCategoria().getNombreCategoria());
            if (categoriaProducto == null){
                throw new MissingCategoriaException();
            }
            return productoRepository.save(new Producto(producto.getNombre(), producto.getDescripcion(), producto.getPrecio(), producto.getStock(), categoriaProducto.get()));
        } else {
            throw new ProductoDuplicateException();
        }
    }
}