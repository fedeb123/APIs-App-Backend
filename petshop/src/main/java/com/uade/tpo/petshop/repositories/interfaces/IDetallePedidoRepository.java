package com.uade.tpo.petshop.repositories.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.petshop.entity.DetallePedido;

@Repository
public interface IDetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    @Query(value="SELECT d FROM DetallePedido d WHERE d.pedido.id = ?1")
    List<DetallePedido> findByPedidoId(Long pedidoId);

    Page<DetallePedido> findByPedidoClienteId(Long clienteId, Pageable pageable);
}
