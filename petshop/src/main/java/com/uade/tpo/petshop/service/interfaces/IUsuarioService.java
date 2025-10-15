package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.UsuarioDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;

public interface IUsuarioService {
    public Optional<Usuario> getUsuarioById(Long id);
    public Page<Usuario> getAllUsuarios(PageRequest pageable);
    public Usuario createUsuario(UsuarioDTO usuario) throws UsuarioDuplicateException, MissingRolException;
    public Optional<Usuario> getUsuarioByEmail(String email);
    public void deleteUsuarioById(Long id) throws MissingUserException; 
    public Usuario updateUsuario(Long id, UsuarioDTO usuario) throws UsuarioDuplicateException, MissingUserException; //aca agrego la U del crud (actualizar usuario)
    public UsuarioDTO getUsuarioConTodoPorId(Long id) throws MissingUserException;
}


// -> esto es el metodo de autenticacion para más adelante

// public interface IUsuarioService {
//     // ...existing code...
//     public Optional<Usuario> authenticateUsuario(String email, String password);
// }
