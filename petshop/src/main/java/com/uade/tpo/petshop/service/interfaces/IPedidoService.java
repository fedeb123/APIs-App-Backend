package com.uade.tpo.petshop.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoDuplicateException;

public interface IPedidoService {

    List<Pedido> findAll();

    Optional<Pedido> findById(Long id);

    Pedido crearPedido(PedidoDTO pedido) throws PedidoDuplicateException, MissingProductoException, MissingUserException;

    Pedido updatePedido(Long id, Pedido pedido) throws PedidoDuplicateException, MissingPedidoException;

    void delete(Long id) throws MissingPedidoException;
}
