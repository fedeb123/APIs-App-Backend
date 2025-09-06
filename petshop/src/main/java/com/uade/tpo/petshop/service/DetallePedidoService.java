package com.uade.tpo.petshop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.repositories.interfaces.IDetallePedidoRepository;
import com.uade.tpo.petshop.service.interfaces.IDetallePedidoService;

import jakarta.transaction.Transactional;

@Service
public class DetallePedidoService implements IDetallePedidoService {
    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    @Override
    public Page<DetallePedido> findAll(PageRequest pageRequest) {
        return detallePedidoRepository.findAll(pageRequest);
    }

    @Override
    public Optional<DetallePedido> findById(Long id) {
        return detallePedidoRepository.findById(id);
    }

    @Override
    @Transactional
    public DetallePedido save(DetallePedidoDTO detallePedidoDTO) {
        DetallePedido detalle = new DetallePedido(
            null, // El pedido debe ser asignado en la lógica del controlador o servicio
            null, // El producto debe ser asignado en la lógica del controlador o servicio
            detallePedidoDTO.getCantidad(),
            detallePedidoDTO.getPrecioSubtotal()
        );
        return detallePedidoRepository.save(detalle);
    }

    @Override
    @Transactional
    public DetallePedido update(Long id, DetallePedidoDTO detallePedidoDTO) {
        return detallePedidoRepository.findById(id)
            .map(d -> {
                d.setCantidad(detallePedidoDTO.getCantidad());
                d.setPrecioSubtotal(detallePedidoDTO.getPrecioSubtotal());
                // Actualizar producto y pedido si corresponde
                return detallePedidoRepository.save(d);
            })
            .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado con id " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new RuntimeException("DetallePedido no encontrado con id " + id);
        }
        detallePedidoRepository.deleteById(id);
    }
}
