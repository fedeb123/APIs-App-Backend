package com.uade.tpo.petshop.service.interfaces;

import com.uade.tpo.petshop.entity.Pedido;

import java.util.List;
import java.util.Optional;

public interface IPedidoService {

    List<Pedido> findAll();

    Optional<Pedido> findById(Long id);

    Pedido save(Pedido pedido);

    Pedido update(Long id, Pedido pedido);

    void delete(Long id);
}
