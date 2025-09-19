package com.uade.tpo.petshop.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.ProductoDuplicateException;
import com.uade.tpo.petshop.service.interfaces.IProductoService;



@RestController
@RequestMapping("api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO producto) throws MissingCategoriaException, MissingUserException, MissingProductoException, ProductoDuplicateException {
        Producto productoNuevo = productoService.createProducto(producto);
        return ResponseEntity.ok(productoNuevo.toDTO());
    }

    @GetMapping
    public ResponseEntity<Page<ProductoDTO>> getAllProductos(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        Page<Producto> productos;
        if (page == null && size == null){
            productos = productoService.getAllProductos(PageRequest.of(0, Integer.MAX_VALUE));
            
        } else {
            productos = productoService.getAllProductos(PageRequest.of(page, size));
        }
        Page<ProductoDTO> productosDTO = productos.map(Producto::toDTO);
        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/validos")
    public ResponseEntity<Page<ProductoDTO>> getAllProductosConStock(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        Page<Producto> productos;
        if (page == null && size == null){
            productos = productoService.getProductosConStock(PageRequest.of(0, Integer.MAX_VALUE));
            
        } else {
            productos = productoService.getProductosConStock(PageRequest.of(page, size));
        }
        Page<ProductoDTO> productosDTO = productos.map(Producto::toDTO);
        return ResponseEntity.ok(productosDTO);

    }

    @GetMapping("/{productoId}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long productoId) {
        Optional<Producto> producto = productoService.getProductoById(productoId);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get().toDTO());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<String> updateProducto(@PathVariable Long productoId, @RequestBody ProductoDTO productoDTO) throws MissingCategoriaException, MissingUserException, MissingProductoException, ProductoDuplicateException {
        productoService.updateProducto(productoId, productoDTO);
        return ResponseEntity.ok("Producto Editado Correctamente");           

    }

    @PutMapping("/actualizarStock/{productoId}")
    public ResponseEntity<String> updateStock(@PathVariable Long productoId, @RequestBody ProductoDTO productoDTO) throws MissingProductoException {
        productoService.updateStock(productoId, productoDTO);
        return ResponseEntity.ok("Stock Actualizado Correctamente");
    }

    @PostMapping("{productoId}/imagen")
    public ResponseEntity<String> subirImagen(@PathVariable Long productoId, @RequestParam("file") MultipartFile file) throws java.io.IOException, MissingProductoException {        
        productoService.subirImagen(productoId, file);
        return ResponseEntity.ok("Imagen Subida Correctamente");
    }

}