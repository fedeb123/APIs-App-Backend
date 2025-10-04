package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingStockException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;

public interface IDetallePedidoService {
    Page<DetallePedido> findAll(PageRequest pageRequest);
    Optional<DetallePedido> findById(Long id);
    DetallePedido save(DetallePedidoDTO detallePedidoDTO) throws MissingPedidoException, MissingProductoException, MissingStockException, PedidoCanceladoException;
    DetallePedido update(Long id, DetallePedidoDTO detallePedidoDTO);
    void delete(Long id);
    Page<DetallePedido> findByUsuarioId(Long id, PageRequest pageRequest);
}
