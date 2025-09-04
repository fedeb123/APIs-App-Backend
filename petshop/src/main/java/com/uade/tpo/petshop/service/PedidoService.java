package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoDuplicateException;
import com.uade.tpo.petshop.repositories.interfaces.IPedidoRepository;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;
import com.uade.tpo.petshop.service.interfaces.IProductoService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private final IPedidoRepository pedidoRepository;

    @Autowired
    private final IProductoService productoService;

    @Autowired
    private final IUsuarioService usuarioService;

    public PedidoService(IPedidoRepository pedidoRepository, IProductoService productoService, IUsuarioService usuarioService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
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
    @Transactional
    //completar
    public Pedido save(PedidoDTO pedido) throws PedidoDuplicateException, MissingProductoException, MissingUserException {
        List<Pedido> pedidos = pedidoRepository.findByClienteAndFechaPedido(pedido.getCliente().getEmail(), pedido.getFecha());
        if (pedidos.isEmpty()){
            Usuario cliente = usuarioService.getUsuarioByEmail(pedido.getCliente().getEmail()).orElseThrow(() -> new MissingUserException());
            
            pedidoRepository.save(new Pedido())
        }
        throw new PedidoDuplicateException();
    }

    @Override
    @Transactional
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
    @Transactional
    public void delete(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con id " + id);
        }
        pedidoRepository.deleteById(id);
    }
}
