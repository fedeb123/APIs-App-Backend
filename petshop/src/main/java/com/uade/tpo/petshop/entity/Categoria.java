package com.uade.tpo.petshop.entity;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.petshop.entity.dtos.CategoriaDTO;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;

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
        List<ProductoDTO> productosDTO = new ArrayList<>();
        for(Producto p : this.productos){
            productosDTO.add(p.toDTO());
        }
        return new CategoriaDTO(this.id, this.nombreCategoria, this.descripcion, productosDTO);
    }

}