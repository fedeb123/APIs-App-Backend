package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Categoria;
import com.uade.tpo.petshop.entity.dtos.CategoriaDTO;
import com.uade.tpo.petshop.entity.exceptions.CategoriaDuplicateException;
import com.uade.tpo.petshop.entity.exceptions.MissingCategoriaException;
import com.uade.tpo.petshop.repositories.interfaces.ICategoriaRepository;
import com.uade.tpo.petshop.service.interfaces.ICategoriaService;

import jakarta.transaction.Transactional;

@Service
public class CategoriaService implements ICategoriaService{
    @Autowired
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
    @Transactional
    public Categoria createCategoria(CategoriaDTO categoriaDTO) throws CategoriaDuplicateException {
        List<Categoria> categorias = categoriaRepository.findByNombreCategoria(categoriaDTO.getNombreCategoria());
        if (categorias.isEmpty()){
            Categoria nuevaCategoria = new Categoria(categoriaDTO.getNombreCategoria(), categoriaDTO.getDescripcion());
            return categoriaRepository.save(nuevaCategoria);
        }
        throw new CategoriaDuplicateException();
    }

    @Override
    public Optional<Categoria> getCategoriaByNombre(String nombreCategoria){
        return categoriaRepository.findByNombreCategoria(nombreCategoria).stream().findFirst();
    }

    @Override
    @Transactional
    public void deleteCategoriaById(Long id) throws MissingCategoriaException {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new MissingCategoriaException());
        categoriaRepository.deleteById(categoria.getId());
    }

    @Override
    @Transactional
    public Categoria updateCategoria(Long id, CategoriaDTO categoria) throws MissingCategoriaException, CategoriaDuplicateException {
        Categoria categoriaAUpdatear = categoriaRepository.findById(id).orElseThrow(() -> new MissingCategoriaException());
        List<Categoria> categorias = categoriaRepository.findByNombreCategoria(categoria.getNombreCategoria());
        if (categorias.isEmpty()){
            categoriaAUpdatear.updateFromDTO(categoria);
            return categoriaRepository.save(categoriaAUpdatear);
        }
        throw new CategoriaDuplicateException();
    }
}
