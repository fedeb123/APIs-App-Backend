package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoDuplicateException;
import com.uade.tpo.petshop.entity.exceptions.PedidoNotFoundException;

public interface IPedidoService {

    public Page<Pedido> getAllPedidos(PageRequest pageable);

    public Optional<Pedido> getPedidoById(Long id);

    public Pedido crearPedido(PedidoDTO pedido) throws PedidoDuplicateException, MissingProductoException, MissingUserException;

    public void agregarDetalleAPedido(DetallePedidoDTO detalle, Long id);

    public void agregarFacturaAPedido(FacturaDTO factura, Long id);

    public Pedido updatePedido(PedidoDTO pedido, Long id) throws PedidoNotFoundException;
}
