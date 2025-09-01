package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.dto.CategoriaDTO;

public interface ICategoriaService {
    public Optional<Categoria> getCategoriaById(Long id);
    public Page<Categoria> getAllCategorias(PageRequest pageRequest);
    public Categoria createCategoria(CategoriaDTO categoria) throws Exception;
    public Optional<Categoria> getCategoriaByNombre(String nombreCategoria);
    public void deleteCategoriaById(Long id);
}
