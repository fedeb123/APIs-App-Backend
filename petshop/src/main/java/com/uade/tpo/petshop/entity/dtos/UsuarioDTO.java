package com.uade.tpo.petshop.entity.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;
    private String direccion;
    private List<ProductoDTO> productos_creados;
    private List<PedidoDTO> pedidos;
    private RolDTO rol;

    public UsuarioDTO(Long id, String nombre, String apellido, String telefono, String email, String password, String direccion, List<ProductoDTO> productos_creados, List<PedidoDTO> pedidos, RolDTO rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.direccion = direccion;
        this.productos_creados = productos_creados;
        this.pedidos = pedidos;
        this.rol = rol;
    }

    public UsuarioDTO() {
    }
}
