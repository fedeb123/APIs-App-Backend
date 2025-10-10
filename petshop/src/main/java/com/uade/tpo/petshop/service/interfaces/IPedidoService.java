package com.uade.tpo.petshop.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.Factura;
import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;
import com.uade.tpo.petshop.entity.exceptions.PedidoDuplicateException;

public interface IPedidoService {

    public Page<Pedido> getAllPedidos(PageRequest pageable);

    public Optional<Pedido> getPedidoById(Long id);

    public List<Pedido> getPedidoByCliente(Long clienteId, PageRequest pageRequest);

    public Pedido crearPedido(PedidoDTO pedido) throws PedidoDuplicateException, MissingProductoException, MissingUserException;

    public Pedido agregarDetalleAPedido(DetallePedido detalle) throws MissingProductoException, MissingPedidoException, PedidoCanceladoException;

    public void agregarFacturaAPedido(Factura factura) throws MissingPedidoException;

    public void cancelarPedido(Long id) throws MissingPedidoException;

    public void updateEstadoPedido(Long id, PedidoDTO pedido) throws MissingPedidoException, PedidoCanceladoException;

    public List<Pedido> getPedidosFromUsuario(String email) throws MissingUserException, MissingPedidoException;

    public void updateConfirmarPedido(Long id, PedidoDTO pedidoDTO) throws MissingPedidoException, PedidoCanceladoException;
}
