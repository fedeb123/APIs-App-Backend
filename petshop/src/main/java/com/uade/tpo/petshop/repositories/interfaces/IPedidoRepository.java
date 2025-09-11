package com.uade.tpo.petshop.repositories.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Pedido;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long>{

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = ?1 AND p.fechaPedido = ?2")
    List<Pedido> findByClienteAndFechaPedido(Long id, Date fechaPedido);


}
