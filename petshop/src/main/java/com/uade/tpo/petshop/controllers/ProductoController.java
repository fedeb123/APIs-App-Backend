package com.uade.tpo.petshop.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.uade.tpo.petshop.entity.Usuario;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> crearProductoConImagen(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoriaId") Long categoriaId,
            @AuthenticationPrincipal Usuario detallesUsuario,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) throws MissingCategoriaException, MissingUserException, MissingProductoException, ProductoDuplicateException, java.io.IOException {

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre(nombre);
        productoDTO.setDescripcion(descripcion);
        productoDTO.setPrecio(precio);
        productoDTO.setStock(stock);
        productoDTO.setCategoriaId(categoriaId);
        productoDTO.setUsuarioId(detallesUsuario.getId());

        Producto productoNuevo = productoService.createProductoConImagen(productoDTO, imagen);

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

    @GetMapping("/stockeados")
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

    @GetMapping("/descontinuados")
    public ResponseEntity<Page<ProductoDTO>> getAllDescontinuados(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        Page<Producto> productos;
        if (page == null && size == null){
            productos = productoService.getAllDescontinuados(PageRequest.of(0, Integer.MAX_VALUE));
            
        } else {
            productos = productoService.getAllDescontinuados(PageRequest.of(page, size));
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

@PutMapping(value = "/{productoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Map<String, String>> updateProductoConImagen(
    @PathVariable Long productoId,
    @RequestParam("nombre") String nombre,
    @RequestParam("descripcion") String descripcion,
    @RequestParam("precio") Double precio,
    @RequestParam("stock") Integer stock,
    @RequestParam("categoriaId") Long categoriaId,
    @AuthenticationPrincipal Usuario detallesUsuario,
    @RequestParam(value = "imagen", required = false) MultipartFile imagen
) throws Exception {
    // Traer y actualizar producto
    ProductoDTO dto = new ProductoDTO();
    dto.setNombre(nombre);
    dto.setDescripcion(descripcion);
    dto.setPrecio(precio);
    dto.setStock(stock);
    dto.setCategoriaId(categoriaId);
    dto.setUsuarioId(detallesUsuario.getId());
    dto.setActivo(true);
    dto.setFechaBaja(null);

    productoService.updateProducto(productoId, dto);

    // Subir imagen si existe
    if (imagen != null && !imagen.isEmpty()) {
        productoService.subirImagen(productoId, imagen);
    }

    return ResponseEntity.ok(Map.of("message", "Producto actualizado correctamente"));
}


    @PutMapping("/actualizarStock/{productoId}")
    public ResponseEntity<String> updateStock(@PathVariable Long productoId, @RequestBody ProductoDTO productoDTO) throws MissingProductoException {
        productoService.updateStock(productoId, productoDTO);
        return ResponseEntity.ok("Stock Actualizado Correctamente");
    }

    @PostMapping(value = "{productoId}/imagen", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirImagen(@PathVariable Long productoId, @RequestParam("file") MultipartFile file) throws java.io.IOException, MissingProductoException {        
        productoService.subirImagen(productoId, file);
        return ResponseEntity.ok("Imagen Subida Correctamente");
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Map<String, Object>> deleteProducto(@PathVariable Long productoId) throws MissingProductoException {
        productoService.deleteProducto(productoId);
        return ResponseEntity.ok(Map.of(
            "message", "Producto Borrado Correctamente",
            "id", productoId
        ));
    }

    @PutMapping("/descontinuados/reactivar/{productoId}")
    public ResponseEntity<Map<String, Object>> reactivarProducto(@PathVariable Long productoId) throws MissingProductoException {
        productoService.reactivarProducto(productoId);
        return ResponseEntity.ok(Map.of(
            "message", "Producto Reactivado Correctamente",
            "id", productoId
        ));
    }
}