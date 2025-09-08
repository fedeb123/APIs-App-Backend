package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.ProductoDuplicateException;
import com.uade.tpo.petshop.repositories.interfaces.IProductoRepository;
import com.uade.tpo.petshop.service.interfaces.ICategoriaService;
import com.uade.tpo.petshop.service.interfaces.IProductoService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class ProductoService implements IProductoService {
    @Autowired
    private final IProductoRepository productoRepository;

    @Autowired
    private final ICategoriaService categoriaService;

    @Autowired
    private final IUsuarioService usuarioService;

    public ProductoService(IProductoRepository productoRepository, ICategoriaService categoriaService, IUsuarioService usuarioService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
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
    public List<Producto> getProductoByName(String name) {
        return productoRepository.findByName(name);
    }

    @Override
    @Transactional
    public Producto createProducto(ProductoDTO producto) throws MissingCategoriaException, ProductoDuplicateException {
        // Implementacion del metodo para crear un nuevo producto
        List<Producto> productos = productoRepository.findByName(producto.getNombre());
        if(productos.isEmpty()){
            Categoria categoriaProducto = categoriaService.getCategoriaByNombre(producto.getCategoria().getNombreCategoria()).orElseThrow(() -> new MissingCategoriaException());
            return productoRepository.save(new Producto(producto.getNombre(), producto.getDescripcion(), producto.getPrecio(), producto.getStock(), categoriaProducto));
        } else {
            throw new ProductoDuplicateException();
        }
    }

    @Override
    @Transactional
    public Producto updateProducto(Long id, ProductoDTO producto) throws MissingCategoriaException, MissingProductoException, ProductoDuplicateException, MissingUserException {
        Producto productoAUpdatear = productoRepository.findById(id).orElseThrow(() -> new MissingProductoException());        
        List<Producto> productos = productoRepository.findByName(producto.getNombre());
        if (productos.isEmpty()) {
            if (producto.getCategoria() != null) {
                //validacion de categoria
                Categoria categoria = categoriaService.getCategoriaByNombre(producto.getCategoria().getNombreCategoria()).orElseThrow(() -> new MissingCategoriaException());
            }

            if (producto.getUsuarioCreador() != null) {
                //validacion de usuario
                Usuario usuario = usuarioService.getUsuarioByEmail(producto.getUsuarioCreador().getEmail()).orElseThrow(() -> new MissingUserException());
            }
            
            productoAUpdatear.updateFromDTO(producto);
            return productoRepository.save(productoAUpdatear);
        } else {
            throw new ProductoDuplicateException();
        }
    }



    //delete
}