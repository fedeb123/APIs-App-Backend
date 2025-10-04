package com.uade.tpo.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;
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

    @PutMapping("/estado/{pedidoId}")/*Actualizo su estado, solo meto el estado nuevo en el dto  */
    public ResponseEntity<String> updateEstadoPedido(@PathVariable Long pedidoId, @RequestBody PedidoDTO pedidoDTO) throws MissingPedidoException, PedidoCanceladoException {
        pedidoService.updateEstadoPedido(pedidoId, pedidoDTO);
        return ResponseEntity.ok(("Pedido Estado Actualizado Correctamente"));
          
    }

    @PutMapping("/cancelar/{pedidoId}")/*En vez de Borrar, actualizo a cancelado, es decir, cambio el estado*/
    public ResponseEntity<String> cancelarPedido(@PathVariable Long pedidoId) throws MissingPedidoException {
        pedidoService.cancelarPedido(pedidoId);
        return ResponseEntity.ok("Pedido Cancelado Correctamente");

    }

    @PostMapping /*Crea un nuevo pedido y devuelve sus datos como DTO*/
    public ResponseEntity<PedidoDTO> create(@RequestBody PedidoDTO pedidoDTO) throws PedidoDuplicateException, MissingProductoException, MissingUserException {
        Pedido nuevoPedido = pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok(nuevoPedido.toDTO());
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<PedidoDTO>> getPedidosFromUsuario(
                    @RequestParam String usuarioEmail, 
                    @AuthenticationPrincipal Usuario detalleUsuario) throws MissingUserException, MissingPedidoException {

        if (detalleUsuario.getEmail().equals(usuarioEmail)){
            List<Pedido> pedidos = pedidoService.getPedidosFromUsuario(usuarioEmail);
            List<PedidoDTO> pedidosDTO = pedidos.stream().map(Pedido::toDTO).toList();
            return ResponseEntity.ok(pedidosDTO);
        }
        return ResponseEntity.status(403).build(); 

    }
    
    
}
