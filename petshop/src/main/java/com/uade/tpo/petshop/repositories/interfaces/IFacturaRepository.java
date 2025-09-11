package com.uade.tpo.petshop.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Factura;

@Repository
public interface IFacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f WHERE f.id = ?1")
    List<Factura> findById(int numero);

}