package com.uade.tpo.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoDuplicateException;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;


@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private final IPedidoService pedidoService;

    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping /*Traigo todos los pedidos y los transformo en DTO*/
    public ResponseEntity<Page<PedidoDTO>> getAllPedidos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Page<Pedido> pedidos;
        if (page == null || size == null) {
            pedidos = pedidoService.getAllPedidos(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            pedidos = pedidoService.getAllPedidos(PageRequest.of(page, size));
        }
        Page<PedidoDTO> pedidosDTO = pedidos.map(Pedido::toDTO);
        return ResponseEntity.ok(pedidosDTO);
    }

    @GetMapping("/{pedidoId}") /*busco pedido por id y los transformo en DTO*/
    public ResponseEntity<PedidoDTO> getById(@PathVariable Long pedidoId) {
        return pedidoService.getPedidoById(pedidoId)
                .map(pedido -> ResponseEntity.ok(pedido.toDTO()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{pedidoId}")/*lo actualizo  */
    public ResponseEntity<PedidoDTO> updatePedido(@PathVariable Long pedidoId, @RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido pedido = pedidoService.updatePedido(pedidoDTO, pedidoId);
            return ResponseEntity.ok(pedido.toDTO());
        } catch (com.uade.tpo.petshop.entity.exceptions.MissingPedidoException e) {
            return ResponseEntity.notFound().build();
        }       
    }

    @PutMapping("/cancelar/{pedidoId}")/*En vez de Borrar, actualizo a cancelado, es decir, cambio el estado*/
    public ResponseEntity<String> cancelarPedido(@PathVariable Long pedidoId, @RequestBody PedidoDTO pedidoDTO) {
            try {
            pedidoService.cancelarPedido(pedidoId);
            return ResponseEntity.ok("Pedido Cancelado Correctamente");
        } catch (com.uade.tpo.petshop.entity.exceptions.MissingPedidoException e) {
            return ResponseEntity.notFound().build();
        } 
    }

    @PostMapping /*Crea un nuevo pedido y devuelve sus datos como DTO*/
    public ResponseEntity<PedidoDTO> create(@RequestBody PedidoDTO pedidoDTO) throws PedidoDuplicateException, MissingProductoException, MissingUserException {
        Pedido nuevoPedido = pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok(nuevoPedido.toDTO());
    }
}
