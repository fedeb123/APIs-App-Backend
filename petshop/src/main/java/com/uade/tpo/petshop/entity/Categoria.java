package com.uade.tpo.petshop.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.uade.tpo.petshop.entity.dtos.CategoriaDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
//SQl Delete intercepta la instruccion de delete del entity manager del JPA y reemplaza el delete
@SQLDelete(sql = "UPDATE categoria SET activo = false, fechaBaja = NOW() WHERE id = ?")
//SQL Restriction tambien intercepta instrucciones del JPA y les agrega una condicion al where
@org.hibernate.annotations.SQLRestriction("activo = true")
@Entity
public class Categoria {

    public Categoria() {
    }

    public Categoria(String nombreCategoria, String descripcion) {
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.activo = true;
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

    @Column
    private boolean activo;

    @Column
    private Date fechaBaja;

    public CategoriaDTO toDTO(){
        return new CategoriaDTO(this.id, this.nombreCategoria, this.descripcion, this.activo, this.fechaBaja);
    }

    public void updateFromDTO(CategoriaDTO categoriaDTO) {
        if (categoriaDTO.getNombreCategoria() != null && !categoriaDTO.getNombreCategoria().isEmpty()) {
            this.nombreCategoria = categoriaDTO.getNombreCategoria();
        }

        if (categoriaDTO.getDescripcion() != null && !categoriaDTO.getDescripcion().isEmpty()) {
            this.descripcion = categoriaDTO.getDescripcion();
        }

        this.activo = categoriaDTO.isActivo();

        if (categoriaDTO.getFechaBaja() != null) {
            this.fechaBaja = categoriaDTO.getFechaBaja();
        }

    }

}