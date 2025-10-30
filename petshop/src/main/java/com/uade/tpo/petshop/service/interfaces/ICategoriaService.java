package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.dtos.CategoriaDTO;
import com.uade.tpo.petshop.entity.exceptions.CategoriaDuplicateException;
import com.uade.tpo.petshop.entity.exceptions.CategoriaEnUsoException;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;

public interface ICategoriaService {
    public Optional<Categoria> getCategoriaById(Long id);
    public Page<Categoria> getAllCategorias(PageRequest pageRequest);
    public Page<Categoria> getDescontinuadas(PageRequest pageRequest);
    public Categoria createCategoria(CategoriaDTO categoria) throws CategoriaDuplicateException;
    public Optional<Categoria> getCategoriaByNombre(String nombreCategoria);
    public void deleteCategoriaById(Long id) throws MissingCategoriaException, CategoriaEnUsoException;
    public void reactivarCategoria(Long id) throws MissingCategoriaException;
    public Categoria updateCategoria(Long id, CategoriaDTO categoria) throws MissingCategoriaException, CategoriaDuplicateException;
}
