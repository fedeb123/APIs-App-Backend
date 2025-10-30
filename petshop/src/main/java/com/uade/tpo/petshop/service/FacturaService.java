package com.uade.tpo.petshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Factura;
import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.exceptions.DuplicateFacturaException;
import com.uade.tpo.petshop.entity.exceptions.MissingFacturaException;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.repositories.interfaces.IFacturaRepository;
import com.uade.tpo.petshop.service.interfaces.IFacturaService;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;

@Service
public class FacturaService implements IFacturaService{

    @Autowired
    private final IFacturaRepository facturaRepository;

    @Autowired
    private final IPedidoService pedidoService;

    public FacturaService(IFacturaRepository facturaRepository, IPedidoService pedidoService){
        this.facturaRepository = facturaRepository;
        this.pedidoService = pedidoService;
    }

    @Override
    public Page<Factura> getAllFacturas(PageRequest pageable) {
        return facturaRepository.findAll(pageable);
    }

    @Override
    public Factura getFacturaById(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Override
    public Factura createFactura(FacturaDTO facturaDTO) throws MissingPedidoException {
        Pedido pedido = pedidoService.getPedidoById(facturaDTO.getPedidoId()).orElseThrow(MissingPedidoException::new);
        Factura factura = new Factura(pedido, pedido.getPrecioTotal(), facturaDTO.getMetodoDePago());
        factura = facturaRepository.save(factura);
        pedidoService.agregarFacturaAPedido(factura);
        return factura;
    }

    @Override
    public Factura updateFactura(Long id, FacturaDTO factura) throws MissingFacturaException, DuplicateFacturaException, MissingPedidoException {
        Factura facturaAUpdatear = facturaRepository.findById(id).orElseThrow(MissingFacturaException::new);
        
        List<Factura> facturasCoincidentes = facturaRepository.findFacturaByPedidoId(factura.getPedidoId());

        if (!facturasCoincidentes.isEmpty()){
            throw new DuplicateFacturaException();
        }

        Pedido pedido = pedidoService.getPedidoById(factura.getPedidoId()).orElseThrow(MissingPedidoException::new);
        
        facturaAUpdatear.updateFromDTO(factura, pedido);
        
        return facturaRepository.save(facturaAUpdatear);
    }

    @Override
    public void deleteFactura(Long id) throws MissingFacturaException{
        Factura facturaAEliminar = facturaRepository.findById(id).orElseThrow(MissingFacturaException::new);
        facturaRepository.delete(facturaAEliminar);
    }

}
