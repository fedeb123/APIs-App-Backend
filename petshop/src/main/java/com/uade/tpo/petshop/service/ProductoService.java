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

    @Override // obtiene los productos con stock mayor a cero
    public Page<Producto> getProductosConStock(PageRequest pageable) {
        return productoRepository.findByStockGreaterThan(0, pageable);
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
        // Validar nombre duplicado
        List<Producto> productos = productoRepository.findByName(producto.getNombre());
        if (!productos.isEmpty()) {
            throw new ProductoDuplicateException();
        }

        // Buscar la categoría por ID
        Categoria categoria = categoriaService.getCategoriaById(producto.getCategoriaId())
            .orElseThrow(MissingCategoriaException::new);

        // Buscar usuario creador por ID
        Usuario usuario = usuarioService.getUsuarioById(producto.getUsuarioId())
            .orElseThrow(RuntimeException::new);

        Producto nuevo = new Producto(
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getStock(),
            categoria
        );
        nuevo.setUsuario_creador(usuario);

        return productoRepository.save(nuevo);
    }
    @Override
    @Transactional
    public Producto updateProducto(Long id, ProductoDTO producto) throws MissingCategoriaException, MissingProductoException, ProductoDuplicateException, MissingUserException {
        Producto productoAUpdatear = productoRepository.findById(id)
            .orElseThrow(MissingProductoException::new);

        List<Producto> productos = productoRepository.findByName(producto.getNombre());
        if (!productos.isEmpty() && !productos.get(0).getId().equals(id)) {
            throw new ProductoDuplicateException();
        }

        Categoria categoria = null;
        if (producto.getCategoriaId() != null) {
            categoria = categoriaService.getCategoriaById(producto.getCategoriaId())
                .orElseThrow(MissingCategoriaException::new);
        }

        Usuario usuario = null;
        if (producto.getUsuarioId() != null) {
            usuario = usuarioService.getUsuarioById(producto.getUsuarioId())
                .orElseThrow(MissingUserException::new);
        }

        productoAUpdatear.updateFromDTO(producto, categoria, usuario);
        return productoRepository.save(productoAUpdatear);
    }



    //delete
}