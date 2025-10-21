package com.uade.tpo.petshop.entity.dtos;

import com.uade.tpo.petshop.entity.enums.MetodoDePagoEnum;

import lombok.Data;

@Data
public class ConfirmacionPedidoDTO {
    private String codigoDescuento;
    private MetodoDePagoEnum metodoDePago;
}