package com.uade.tpo.petshop.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;


import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.service.interfaces.IDetallePedidoService;

@RestController
@RequestMapping("api/detalle-pedidos")
public class DetallePedidoController {
    @Autowired
    private IDetallePedidoService detallePedidoService;

    @GetMapping
    public ResponseEntity<Page<DetallePedido>> getAllDetallePedidos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            return ResponseEntity.ok(
                detallePedidoService.findAll(PageRequest.of(0, Integer.MAX_VALUE))
            );
        }
        return ResponseEntity.ok(
            detallePedidoService.findAll(PageRequest.of(page, size))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> getDetallePedidoById(@PathVariable Long id) {
        Optional<DetallePedido> detalle = detallePedidoService.findById(id);
        return detalle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DetallePedido> createDetallePedido(@RequestBody DetallePedidoDTO detallePedidoDTO) {
        DetallePedido nuevoDetalle = detallePedidoService.save(detallePedidoDTO);
        return ResponseEntity.created(URI.create("/detalle-pedidos/" + nuevoDetalle.getId())).body(nuevoDetalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> updateDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoDTO detallePedidoDTO) {
        DetallePedido actualizado = detallePedidoService.update(id, detallePedidoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable Long id) {
        detallePedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
