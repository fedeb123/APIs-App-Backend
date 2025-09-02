package com.uade.tpo.petshop.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Rol;


/*
 * Al extender JPA ya se estan incorporando metodos CRUD
 * por ejemplo findById, save, etc
 * Al querer poner un metodo personalizado, agregarlo de la forma:
 * --
 * @Query(value = "Poner la Query") 
 * TipoDeDatoADevolver funcion parametros();
 * --
 * @fedeb123
 */

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {
    
    @Query(value = "SELECT r FROM Rol r WHERE r.nombre =?1")
    List<Rol> findByName(String name);

}