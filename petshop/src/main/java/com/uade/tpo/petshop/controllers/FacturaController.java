package com.uade.tpo.petshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.uade.tpo.petshop.entity.Factura;
import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.exceptions.DuplicateFacturaException;
import com.uade.tpo.petshop.entity.exceptions.MissingFacturaException;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.service.interfaces.IFacturaService;

@RestController
@RequestMapping("api/facturas")
public class FacturaController {

    @Autowired
    private final IFacturaService facturaService;

    public FacturaController(IFacturaService facturaService){
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<Page<FacturaDTO>> getAllFacturas(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        Page<Factura> facturas;
        
        if (page == null || size == null) {
            facturas = facturaService.getAllFacturas(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            facturas = facturaService.getAllFacturas(PageRequest.of(page, size));
        }

        Page<FacturaDTO> facturaDTOs = facturas.map(Factura::toDTO);
        return ResponseEntity.ok(facturaDTOs);
    }
    
    @GetMapping("/{facturaId}")
    public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable Long facturaId) {
        Factura factura = facturaService.getFacturaById(facturaId);
        if (factura != null) {
            return ResponseEntity.ok(factura.toDTO());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> createFactura(@RequestBody FacturaDTO factura) throws MissingPedidoException {
        Factura nuevaFactura = facturaService.createFactura(factura);
        return ResponseEntity.ok(nuevaFactura.toDTO());
    }

    @PutMapping("/{facturaId}")
    public ResponseEntity<FacturaDTO> updateFactura(@PathVariable Long facturaId, @RequestBody FacturaDTO factura) throws MissingFacturaException, DuplicateFacturaException, MissingPedidoException {
        Factura facturaActualizada = facturaService.updateFactura(facturaId, factura);
        if (facturaActualizada != null) {
            return ResponseEntity.ok(facturaActualizada.toDTO());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{facturaId}")
    public ResponseEntity<String> deleteFactura(@PathVariable Long facturaId) throws MissingFacturaException {
        facturaService.deleteFactura(facturaId);
        return ResponseEntity.ok("Factura Eliminada Correctamente");
    }
}