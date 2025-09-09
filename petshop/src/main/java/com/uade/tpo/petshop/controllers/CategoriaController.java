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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.dtos.CategoriaDTO;
import com.uade.tpo.petshop.entity.exceptions.CategoriaDuplicateException;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.service.interfaces.ICategoriaService;




@RestController
@RequestMapping("api/categorias")
public class CategoriaController {
    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping /* @kiki -> Traigo todas las categorias y las transformo en DTO para enviar los datos al cliente*/
    public ResponseEntity<Page<CategoriaDTO>> getAllCategorias(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Page<Categoria> categorias;
        if (page == null || size == null) {
            categorias = categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            categorias = categoriaService.getAllCategorias(PageRequest.of(page, size));
        }
        Page<CategoriaDTO> categoriasDTO = categorias.map(Categoria::toDTO);
        return ResponseEntity.ok(categoriasDTO);
    }

    @GetMapping("/id/{categoriaId}") /*Traigo una categoria segun su ID y lo transformo en dto*/
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Long categoriaId) {
        Optional<Categoria> categoria = categoriaService.getCategoriaById(categoriaId);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get().toDTO());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaDTO> getCategoriaByNombre(@PathVariable String nombre) {
        Optional<Categoria> categoria = categoriaService.getCategoriaByNombre(nombre);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get().toDTO());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping /*creo una nueva categoria */
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestBody CategoriaDTO categoriaDTO) throws CategoriaDuplicateException {
        Categoria result = categoriaService.createCategoria(categoriaDTO);
        return ResponseEntity.created(URI.create("/api/categorias/" + result.getId())).body(result.toDTO());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) throws MissingCategoriaException, CategoriaDuplicateException {
        Categoria categoriaActualizada = categoriaService.updateCategoria(id, categoriaDTO);
        return ResponseEntity.ok(categoriaActualizada.toDTO());
    }

    @DeleteMapping("/{categoriaId}") /*elimino una categoria segun el id, sin DTO, solo confitma la eliminaciòn */
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long categoriaId) throws MissingCategoriaException {
        categoriaService.deleteCategoriaById(categoriaId);
        return ResponseEntity.noContent().build();
    }
    
}

/*@kiki EXPLICACION -> deleteCategoria no devuelve un Data Transfer Object ya que solo
elimina la categoria y no necesita enviar ningun dato al cliente, solo confitma que la operacion fue exitosa.
*/
