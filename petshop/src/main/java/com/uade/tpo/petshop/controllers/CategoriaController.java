package com.uade.tpo.petshop.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.dto.CategoriaDTO;
import com.uade.tpo.petshop.service.interfaces.ICategoriaService;



@RestController
@RequestMapping("api/categorias")
public class CategoriaController {
    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping /*Traigo todas las categorias */
    public ResponseEntity<Page<Categoria>> getAllCategorias(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            return ResponseEntity.ok(
                categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE))
            );
        }
        return ResponseEntity.ok(
            categoriaService.getAllCategorias(PageRequest.of(page, size))
        );
    }

    @GetMapping("/{categoriaId}") /*Traigo una categoria segun su ID */
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria=categoriaService.getCategoriaById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar") /*busco una categoria segun el nombre */
    public ResponseEntity<Categoria> getCategoriaByNombre(@RequestParam String nombre) {
        return categoriaService.getCategoriaByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping /*creo una nueva categoria */
    public ResponseEntity<Object> createCategoria(@RequestBody CategoriaDTO categoriaRequest) throws Exception {
        Categoria result = categoriaService.createCategoria(categoriaRequest);
        return ResponseEntity.created(URI.create("/api/categorias/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}") /*elimino una categoria segun el id */
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoriaById(id);
        return ResponseEntity.noContent().build();
    }
    
}
