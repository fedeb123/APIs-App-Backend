package com.uade.tpo.petshop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.Factura;
import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.enums.EstadoEnum;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;
import com.uade.tpo.petshop.repositories.interfaces.IPedidoRepository;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private final IPedidoRepository pedidoRepository;

    @Autowired
    private final IUsuarioService usuarioService;

    public PedidoService(IPedidoRepository pedidoRepository, IUsuarioService usuarioService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public Page<Pedido> getAllPedidos(PageRequest pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Override
    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pedido crearPedido(PedidoDTO pedidoDTO) throws MissingProductoException, MissingUserException {
        Usuario cliente = usuarioService.getUsuarioById(pedidoDTO.getClienteId()).orElseThrow(MissingUserException::new);
        Pedido pedido = new Pedido(cliente, EstadoEnum.PENDIENTE);
        return pedidoRepository.save(pedido); // primero guardo el pedido
    }

    @Override
    @Transactional
    public void agregarDetalleAPedido(DetallePedido detalle) throws MissingProductoException, MissingPedidoException {
        Pedido pedido = pedidoRepository.findById(detalle.getPedido().getId()).orElseThrow(MissingPedidoException::new);

        // Agrego el detalle al pedido
        pedido.agregarDetalle(detalle);

        // Persiste el cambio
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void agregarFacturaAPedido(Factura factura) throws MissingPedidoException  {
        Pedido pedido = pedidoRepository.findById(factura.getPedido().getId()).orElseThrow(() -> new MissingPedidoException());
        pedido.agregarFactura(factura);
        pedidoRepository.save(pedido);
    }


    @Override
    @Transactional
    public void updateEstadoPedido(Long id, PedidoDTO pedidoDTO) throws MissingPedidoException, PedidoCanceladoException {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(MissingPedidoException::new);

        if (pedido.getEstado() == EstadoEnum.CANCELADO){
            throw new PedidoCanceladoException();
        }
        
        pedido.setEstado(pedidoDTO.getEstado());
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void cancelarPedido(Long id) throws MissingPedidoException {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new MissingPedidoException());
        pedido.setEstado(EstadoEnum.CANCELADO);
        pedidoRepository.save(pedido);
    }

}
