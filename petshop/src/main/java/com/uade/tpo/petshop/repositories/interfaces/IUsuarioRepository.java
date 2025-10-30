package com.uade.tpo.petshop.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query(value="SELECT u FROM Usuario u WHERE u.email = ?1")
    List<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u " +
       "LEFT JOIN FETCH u.pedidos " +
       "LEFT JOIN FETCH u.productos_creados " +
       "LEFT JOIN FETCH u.rol " +
       "WHERE u.id = :id")
    Optional<Usuario> findByIdWithPedidosProductosAndRol(Long id);

    @Query(value="SELECT u FROM Usuario u WHERE u.email = ?1")
    Optional<Usuario> findByEmailPersonalData(String email);
}
