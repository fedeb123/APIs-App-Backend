package com.uade.tpo.petshop.entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.uade.tpo.petshop.entity.dtos.PedidoDTO;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.dtos.UsuarioDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
public class Usuario implements UserDetails {
    public Usuario() {
    }

    public Usuario(String nombre, String apellido,String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Builder
    public Usuario(String nombre, String apellido, String telefono, String email, String password, String direccion, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
        this.pedidos = new ArrayList<>();
        this.productos_creados = new ArrayList<>();
        this.rol = rol;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long id;

    @Column
    private String nombre;
    
    @Column
    private String apellido;
    
    @Column
    private String telefono;

    @Column
    private String email;

    @Column
    private String password;
    
    @Column
    private String direccion;

    @OneToMany(mappedBy="usuario_creador")
    private List<Producto> productos_creados;
    
    @OneToMany(mappedBy="cliente")
    List<Pedido> pedidos;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public UsuarioDTO toDTO(){
        List<PedidoDTO> pedidosDTOs = new ArrayList<>();
        if (pedidosDTOs != null){
            for (Pedido p : this.pedidos){
                pedidosDTOs.add(p.toDTO());
            }
        }

        List<ProductoDTO> productosDTOs = new ArrayList<>();
        if (productosDTOs != null){
            for (Producto pr : this.productos_creados){
                productosDTOs.add(pr.toDTO());
            }
        }

        return new UsuarioDTO(this.id, this.nombre, this.apellido, this.telefono, this.email, this.password, this.direccion, productosDTOs, pedidosDTOs, this.rol.toDTO());
    }

    public void updateFromDTO(UsuarioDTO usuario){
        if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
            this.setNombre(usuario.getNombre());
        }
        if (usuario.getApellido() != null && !usuario.getApellido().isEmpty()) {
            this.setApellido(usuario.getApellido());
        }
        if (usuario.getTelefono() != null && !usuario.getTelefono().isEmpty()) {
            this.setTelefono(usuario.getTelefono());
        }
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            this.setEmail(usuario.getEmail());
        }
        if (usuario.getDireccion() != null && !usuario.getDireccion().isEmpty()) {
            this.setDireccion(usuario.getDireccion());
        }
        
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(rol.getNombre().toString()));
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }



}
