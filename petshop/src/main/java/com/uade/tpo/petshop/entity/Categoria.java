package com.uade.tpo.petshop.entity;

import java.util.List;

import com.uade.tpo.petshop.entity.dtos.CategoriaDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Categoria {

    public Categoria() {
    }

    public Categoria(String nombreCategoria, String descripcion) {
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String nombreCategoria;
    
    @Column
    private String descripcion;

    @OneToMany(mappedBy="categoria")
    List<Producto> productos;

    public CategoriaDTO toDTO(){
        return new CategoriaDTO(this.id, this.nombreCategoria, this.descripcion);
    }

    public void updateFromDTO(CategoriaDTO categoriaDTO) {
        if (categoriaDTO.getNombreCategoria() != null && !categoriaDTO.getNombreCategoria().isEmpty()) {
            this.nombreCategoria = categoriaDTO.getNombreCategoria();
        }

        if (categoriaDTO.getDescripcion() != null && !categoriaDTO.getDescripcion().isEmpty()) {
            this.descripcion = categoriaDTO.getDescripcion();
        }

    }

}