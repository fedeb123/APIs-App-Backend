package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.enums.EstadoEnum;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
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
    public Page<Pedido> getAllPedidos(PageRequest pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Override
    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional
    //completar
    public Pedido crearPedido(PedidoDTO pedido) throws PedidoDuplicateException, MissingProductoException, MissingUserException {
        List<Pedido> pedidos = pedidoRepository.findByClienteAndFechaPedido(pedido.getClienteId(), pedido.getFechaPedido());
        if (pedidos.isEmpty()){
            Usuario cliente = usuarioService.getUsuarioByEmail(pedido.getClienteId()).orElseThrow(() -> new MissingUserException());
            return pedidoRepository.save(new Pedido(cliente, pedido.getFechaPedido(), pedido.getEstado()));
        }
        throw new PedidoDuplicateException();
    }

    @Override
    @Transactional
    public void agregarDetalleAPedido(DetallePedidoDTO detalle, Long id) throws MissingProductoException, MissingPedidoException {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(MissingPedidoException::new);
        Producto producto = productoService.getProductoById(detalle.getProducto().getId()).orElseThrow(MissingProductoException::new);
        pedido.agregarDetalle(producto, detalle.getCantidad());
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void agregarFacturaAPedido(FacturaDTO factura, Long id) throws MissingPedidoException  {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new MissingPedidoException());
        pedido.agregarFactura(factura);
        pedidoRepository.save(pedido);
    }


    @Override
    @Transactional
    public Pedido updatePedido(PedidoDTO pedidoDTO, Long id) throws MissingPedidoException {
        return pedidoRepository.findById(id)
            .map(p->{
                p.setEstado(pedidoDTO.getEstado());
                return pedidoRepository.save(p);
            })
            .orElseThrow(()-> new MissingPedidoException());
    }

    @Override
    @Transactional
    public void cancelarPedido(Long id) throws MissingPedidoException {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new MissingPedidoException());
        pedido.setEstado(EstadoEnum.CANCELADO);
        pedidoRepository.save(pedido);
    }

}
