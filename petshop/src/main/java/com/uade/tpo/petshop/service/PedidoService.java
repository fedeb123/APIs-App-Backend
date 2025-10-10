package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.Factura;
import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.enums.EstadoEnum;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;
import com.uade.tpo.petshop.repositories.interfaces.IPedidoRepository;
import com.uade.tpo.petshop.repositories.interfaces.IProductoRepository;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private final IPedidoRepository pedidoRepository;

    @Autowired
    private final IUsuarioService usuarioService;

    @Autowired
    private final IProductoRepository productoRepository;
    public PedidoService(IPedidoRepository pedidoRepository, IUsuarioService usuarioService,IProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioService = usuarioService;
        this.productoRepository=productoRepository;
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
    public List<Pedido> getPedidoByCliente(Long clienteId, PageRequest pageRequest){
        return pedidoRepository.findByCliente(clienteId);
    }

    @Override
    @Transactional
    public Pedido crearPedido(PedidoDTO pedidoDTO) throws MissingProductoException, MissingUserException {
        Usuario cliente = usuarioService.getUsuarioById(pedidoDTO.getClienteId()).orElseThrow(MissingUserException::new);
        Pedido pedido = new Pedido(cliente, EstadoEnum.PENDIENTE);
        // Si el DTO trae detalles, los agregamos
        if (pedidoDTO.getDetalles() != null && !pedidoDTO.getDetalles().isEmpty()) {
            for (DetallePedidoDTO detDTO : pedidoDTO.getDetalles()) {
                
                Producto producto = productoRepository.findById(detDTO.getProductoId())
                        .orElseThrow(MissingProductoException::new);

                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setProducto(producto);
                detalle.setCantidad(detDTO.getCantidad());
                detalle.setPrecioSubtotal(producto.getPrecio() * detDTO.getCantidad());
                pedido.agregarDetalle(detalle);
            }
        }
        return pedidoRepository.save(pedido); // primero guardo el pedido
    }

    @Override
    @Transactional
    public Pedido agregarDetalleAPedido(DetallePedido detalle) 
            throws MissingProductoException, MissingPedidoException, PedidoCanceladoException {

        Pedido pedido = pedidoRepository.findById(detalle.getPedido().getId())
                .orElseThrow(MissingPedidoException::new);

        // if (pedido.getEstado() == EstadoEnum.CANCELADO) {
        //     throw new PedidoCanceladoException();
        // }

        if (detalle.getProducto() == null) {
            throw new MissingProductoException();
        }

        pedido.agregarDetalle(detalle);

        // Persiste el cambio
        return pedidoRepository.save(pedido);
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

        // if (pedido.getEstado() == EstadoEnum.CANCELADO){
        //     throw new PedidoCanceladoException();
        // }
        
        pedido.setEstado(pedidoDTO.getEstado());
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void updateConfirmarPedido(Long id, PedidoDTO pedidoDTO) throws MissingPedidoException, PedidoCanceladoException {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(MissingPedidoException::new);

        // if (pedido.getEstado() == EstadoEnum.CANCELADO){
        //     throw new PedidoCanceladoException();
        // }
        
        pedido.setEstado(EstadoEnum.CONFIRMADO);
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void cancelarPedido(Long id) throws MissingPedidoException {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new MissingPedidoException());
        // pedido.setEstado(EstadoEnum.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> getPedidosFromUsuario(String email) throws MissingUserException, MissingPedidoException {
        Usuario usuario = usuarioService.getUsuarioByEmail(email).orElseThrow(MissingUserException::new);

        List<Pedido> pedidos = pedidoRepository.findByCliente(usuario.getId());

        if (pedidos.isEmpty()) {
            throw new MissingPedidoException();
        }
        return pedidos;
    }



}
