package com.uade.tpo.petshop.service.impl;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.repositories.PedidoRepository;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido update(Long id, Pedido pedido) {
        return pedidoRepository.findById(id)
                .map(p -> {
                    p.setCliente(pedido.getCliente());
                    p.setFechaPedido(pedido.getFechaPedido());
                    p.setEstado(pedido.getEstado());
                    return pedidoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
    }

    @Override
    public void delete(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con id " + id);
        }
        pedidoRepository.deleteById(id);
    }
}
