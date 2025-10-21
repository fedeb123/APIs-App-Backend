package com.uade.tpo.petshop.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public Page<Producto> getAllDescontinuados(PageRequest pageable) {
        return productoRepository.findDescontinuados(pageable);
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
    public Producto createProductoConImagen(ProductoDTO productoDTO, MultipartFile imagen)
            throws MissingCategoriaException, ProductoDuplicateException, MissingUserException, java.io.IOException {

        // 1. Crear producto sin imagen
        Producto productoNuevo = createProducto(productoDTO);

        // 2. Subir imagen si existe
        if (imagen != null && !imagen.isEmpty()) {
            String directorioUpload = "uploads/productos/";
            String nombreArchivo = productoNuevo.getId() + "_" + imagen.getOriginalFilename();
            Path ruta = Paths.get(directorioUpload, nombreArchivo);

            Files.createDirectories(ruta.getParent());
            Files.write(ruta, imagen.getBytes());

            productoNuevo.setImageUrl("/" + directorioUpload + nombreArchivo);
            productoRepository.save(productoNuevo);
        }

        return productoNuevo;
    }

    @Override
    @Transactional
    public Producto createProducto(ProductoDTO producto) throws MissingCategoriaException, ProductoDuplicateException, MissingUserException {
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
            .orElseThrow(MissingUserException::new);

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

    @Override
    @Transactional
    public void updateStock(Long productoId, ProductoDTO producto) throws MissingProductoException{
        Producto productoAUpdatear = productoRepository.findById(productoId).orElseThrow(MissingProductoException::new);
        
        productoAUpdatear.setStock(producto.getStock());
        
        productoRepository.save(productoAUpdatear);
    }

    @Override
    @Transactional
    public void subirImagen(Long productoId, MultipartFile file) throws MissingProductoException, java.io.IOException {

        Producto producto = this.getProductoById(productoId).orElseThrow(MissingProductoException::new);

        String directorioUpload = "uploads/productos/";
        String nombreArchivo = productoId + "_" + file.getOriginalFilename();
        Path ruta = Paths.get(directorioUpload, nombreArchivo);

        //Puede provocar java.io.IOException
        Files.createDirectories(ruta.getParent());
        Files.write(ruta, file.getBytes());
        
        producto.setImageUrl("/" + directorioUpload + nombreArchivo);
        productoRepository.save(producto);
        
    }

    //delete
    @Override
    @Transactional
    public void deleteProducto(Long id) throws MissingProductoException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(MissingProductoException::new);
        productoRepository.delete(producto);
    }

    @Override
    @Transactional
    public void reactivarProducto(Long id) throws MissingProductoException {
        int filasAfectadas = productoRepository.reactivarById(id);
        if (filasAfectadas == 0) {
            throw new MissingProductoException();
        }
    }
}