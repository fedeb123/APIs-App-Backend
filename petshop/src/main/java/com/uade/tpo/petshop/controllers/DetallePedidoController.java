package com.uade.tpo.petshop.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.enums.RolEnum;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingStockException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;
import com.uade.tpo.petshop.service.interfaces.IDetallePedidoService;

@RestController
@RequestMapping("api/detalle-pedidos")
public class DetallePedidoController {
    @Autowired
    private IDetallePedidoService detallePedidoService;

    @GetMapping /*agarro todos los detalles de pedido y los transf en DTO */
    public ResponseEntity<Page<DetallePedidoDTO>> getAllDetallePedidos(
            @AuthenticationPrincipal Usuario detallesUsuario,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (detallesUsuario.getRol().equals(RolEnum.ADMIN)){
            Page<DetallePedido> detalles;
            if (page == null || size == null) {
                detalles = detallePedidoService.findAll(PageRequest.of(0, Integer.MAX_VALUE));
            } else {
                detalles = detallePedidoService.findAll(PageRequest.of(page, size));
            }
            Page<DetallePedidoDTO> detallesDTO = detalles.map(DetallePedido::toDTO);
            return ResponseEntity.ok(detallesDTO);
        }
        else{
            Page<DetallePedido> detalles;
            if (page == null || size == null) {
                detalles = detallePedidoService.findByUsuarioId(detallesUsuario.getId(), PageRequest.of(0,Integer.MAX_VALUE));
            } else {
                detalles = detallePedidoService.findByUsuarioId(detallesUsuario.getId(), PageRequest.of(page,size));
            }
            Page<DetallePedidoDTO> detallesDTO = detalles.map(DetallePedido::toDTO);
            return ResponseEntity.ok(detallesDTO);
        }
    }

    @GetMapping("/{id}") /*busco detalle del pedido por ID y lo transf en DTO */
    public ResponseEntity<DetallePedidoDTO> getDetallePedidoById(@PathVariable Long id) {
        Optional<DetallePedido> detalle = detallePedidoService.findById(id);
        return detalle.map(d -> ResponseEntity.ok(d.toDTO()))
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping /*devuelve el detalle del pedido recien creado en forma de DTO */
    public ResponseEntity<DetallePedidoDTO> createDetallePedido(@RequestBody DetallePedidoDTO detallePedidoDTO) throws MissingProductoException, MissingPedidoException, MissingStockException, PedidoCanceladoException {
        DetallePedido nuevoDetalle = detallePedidoService.save(detallePedidoDTO);
        return ResponseEntity.created(URI.create("/detalle-pedidos/" + nuevoDetalle.getId()))
                             .body(nuevoDetalle.toDTO());
    }

    @PutMapping("/{id}") /*actualiza el detalle del pedido y lo devuelve en forma de DTO */
    public ResponseEntity<DetallePedidoDTO> updateDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoDTO detallePedidoDTO) {
        DetallePedido actualizado = detallePedidoService.update(id, detallePedidoDTO);
        return ResponseEntity.ok(actualizado.toDTO());
    }

    @DeleteMapping("/{id}") /*misma explicacion que categoria controller, no devuelve datos, solo confirma la eliminacion*/
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable Long id,  @AuthenticationPrincipal Usuario detallesUsuario) {
        Optional<DetallePedido> detalle=detallePedidoService.findById(id);
        if (detalle.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        DetallePedido detallePedido = detalle.get();
        boolean pertenece = detallesUsuario.getPedidos().stream()
            .anyMatch(p -> p.getId().equals(detallePedido.getPedido().getId()));
        
        if (pertenece){
            detallePedidoService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).build();

    }
}
