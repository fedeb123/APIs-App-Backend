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

    @GetMapping // Traigo todos los pedidos
    public ResponseEntity<Page<Pedido>> getAllPedidos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            return ResponseEntity.ok(
                pedidoService.getAllPedidos(PageRequest.of(0, Integer.MAX_VALUE))
            );
        }
        return ResponseEntity.ok(
            pedidoService.getAllPedidos(PageRequest.of(page, size))
        );
    }

    @GetMapping("/{pedidoId}") //Muestro un pedido por id
    public ResponseEntity<Pedido> getById(@PathVariable Long pedidoId) {
        return pedidoService.getPedidoById(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")/*Cambio de ESTADO en un pedido */
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        Pedido pedido=pedidoService.updatePedido(pedidoDTO, id);
        return ResponseEntity.ok(pedido);
        
        
    }

    @PostMapping //Creo UN nuevo pedido
    public ResponseEntity<Pedido> create(@RequestBody Pedido pedido) throws PedidoDuplicateException, MissingProductoException, MissingUserException {
        return ResponseEntity.ok(pedidoService.crearPedido(pedido.toDTO()));
    }

}
