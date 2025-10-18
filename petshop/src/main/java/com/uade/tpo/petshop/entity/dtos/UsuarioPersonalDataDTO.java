package com.uade.tpo.petshop.entity.dtos;
import lombok.Data;

@Data
public class UsuarioPersonalDataDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    private RolDTO rol;

    public UsuarioPersonalDataDTO (Long id, String nombre, String apellido, String telefono, String email, String direccion, RolDTO rol){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.rol = rol;
        this.direccion = direccion;
    }

    public UsuarioPersonalDataDTO(){}
}
