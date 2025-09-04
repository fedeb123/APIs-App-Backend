package com.uade.tpo.petshop.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;

public interface IDetallePedidoService {
    List<DetallePedido> findAll();
    Optional<DetallePedido> findById(Long id);
    DetallePedido save(DetallePedidoDTO detallePedidoDTO);
    DetallePedido update(Long id, DetallePedidoDTO detallePedidoDTO);
    void delete(Long id);
}
