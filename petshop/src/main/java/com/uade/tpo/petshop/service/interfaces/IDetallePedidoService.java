package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;

public interface IDetallePedidoService {
    Page<DetallePedido> findAll(PageRequest pageRequest);
    Optional<DetallePedido> findById(Long id);
    DetallePedido save(DetallePedidoDTO detallePedidoDTO);
    DetallePedido update(Long id, DetallePedidoDTO detallePedidoDTO);
    void delete(Long id);
}
