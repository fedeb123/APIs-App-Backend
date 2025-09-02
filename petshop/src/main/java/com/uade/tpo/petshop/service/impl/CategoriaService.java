package com.uade.tpo.petshop.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.dto.CategoriaDTO;
import com.uade.tpo.petshop.repositories.interfaces.ICategoriaRepository;
import com.uade.tpo.petshop.service.interfaces.ICategoriaService;


@Service
public class CategoriaService implements ICategoriaService{
    
    
    private final ICategoriaRepository categoriaRepository;

    public CategoriaService(ICategoriaRepository categoriaRepository){
        this.categoriaRepository=categoriaRepository;
    }
    
    @Override
    public Optional<Categoria> getCategoriaById(Long categoriaId){
        return categoriaRepository.findById(categoriaId);
    }

    @Override
    public Page<Categoria> getAllCategorias(PageRequest pageRequest){
        return categoriaRepository.findAll(pageRequest);
    }

    @Override
    public Categoria createCategoria(CategoriaDTO categoriaDTO) throws Exception {
        if (categoriaDTO.getNombreCategoria() == null || categoriaDTO.getNombreCategoria().isEmpty()) {
            throw new Exception("El nombre de la categoría no puede estar vacío");
        }

        Categoria nuevaCategoria = new Categoria(
            categoriaDTO.getNombreCategoria(),
            categoriaDTO.getDescripcion()
        );

        return categoriaRepository.save(nuevaCategoria);
    }

    @Override
    public Optional<Categoria> getCategoriaByNombre(String nombreCategoria){
        return categoriaRepository.findByNombreCategoria(nombreCategoria).stream().findFirst();
    }

    @Override
    public void deleteCategoriaById(Long id){
        categoriaRepository.deleteById(id);
    }
}
