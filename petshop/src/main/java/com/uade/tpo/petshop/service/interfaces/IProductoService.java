package com.uade.tpo.petshop.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.ProductoDuplicateException;

public interface IProductoService {
    public Page<Producto> getAllProductos(PageRequest pageable);
    public Page<Producto> getAllDescontinuados(PageRequest pageable);
    public Page<Producto> getProductosConStock(PageRequest pageable);
    public Optional<Producto> getProductoById(Long id);
    public List<Producto> getProductoByName(String name);
    public Producto createProducto(ProductoDTO producto) throws MissingCategoriaException, ProductoDuplicateException, MissingUserException;
    public void updateStock(Long id, ProductoDTO producto) throws MissingProductoException;
    public Producto updateProducto(Long id, ProductoDTO producto) throws MissingCategoriaException, MissingProductoException, ProductoDuplicateException, MissingUserException;
    public void subirImagen(Long id, MultipartFile file) throws java.io.IOException, MissingProductoException;
    public void deleteProducto(Long id) throws MissingProductoException;
    public Producto createProductoConImagen(ProductoDTO productoDTO, MultipartFile imagen)
            throws MissingCategoriaException, ProductoDuplicateException, MissingUserException, java.io.IOException;
}
