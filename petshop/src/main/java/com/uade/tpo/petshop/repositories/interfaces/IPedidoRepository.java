package com.uade.tpo.petshop.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.Pedido;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
}
