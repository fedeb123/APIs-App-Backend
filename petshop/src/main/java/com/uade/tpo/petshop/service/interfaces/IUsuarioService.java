package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.UsuarioDTO;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;

public interface IUsuarioService {
    public Optional<Usuario> getUsuarioById(Long id);
    public Page<Usuario> getAllUsuarios(PageRequest pageable);
    public Usuario createUsuario(UsuarioDTO usuario) throws UsuarioDuplicateException;
    public Optional<Usuario> getUsuarioByEmail(String email);
    public void deleteUsuarioById(Long id); 
}
