package com.uade.tpo.petshop.service;

import com.uade.tpo.petshop.models.Factura;
import com.uade.tpo.petshop.repositories.interfaces.IFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private IFacturaRepository facturaRepository;

    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }

    public Factura getFacturaById(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    public Factura createFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    public Factura updateFactura(Long id, Factura factura) {
        if (facturaRepository.existsById(id)) {
            factura.setId(id);
            return facturaRepository.save(factura);
        }
        return null;
    }

    public boolean deleteFactura(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
