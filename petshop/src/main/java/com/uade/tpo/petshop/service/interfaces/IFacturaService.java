package com.uade.tpo.petshop.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Factura;
import com.uade.tpo.petshop.entity.dtos.FacturaDTO;
import com.uade.tpo.petshop.entity.exceptions.DuplicateFacturaException;
import com.uade.tpo.petshop.entity.exceptions.MissingFacturaException;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;

public interface IFacturaService {
    public Page<Factura> getAllFacturas(PageRequest pageable);
    public Factura getFacturaById(Long id);
    public Factura createFactura(FacturaDTO factura) throws MissingPedidoException;
    public Factura updateFactura(Long id, FacturaDTO factura) throws MissingFacturaException, DuplicateFacturaException, MissingPedidoException;
    public void deleteFactura(Long id) throws MissingFacturaException;
}
