package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.UsuarioDTO;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.repositories.interfaces.IRolRepository;
import com.uade.tpo.petshop.repositories.interfaces.IUsuarioRepository;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;
import com.uade.tpo.petshop.entity.Rol;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IRolRepository rolRepository;

    

    @Override
    public Page<Usuario> getAllUsuarios(PageRequest pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId);

    }


//comentario a discusion: ver si nos manejamos por email o por id

    @Override
    public Optional<Usuario> getUsuarioByEmail(String email) {
        List<Usuario> usuarios = usuarioRepository.findByEmail(email);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }


    @Override
    @Transactional
    public void deleteUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario updateUsuario(Long id, UsuarioDTO usuario) throws UsuarioDuplicateException {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        
        if (usuarioExistente.isPresent()) {
            Usuario userToUpdate = usuarioExistente.get();
            
            // Verificar si el nuevo email ya existe en otro usuario
            if (!userToUpdate.getEmail().equals(usuario.getEmail())) {
                List<Usuario> usuariosConEmail = usuarioRepository.findByEmail(usuario.getEmail());
                if (!usuariosConEmail.isEmpty()) {
                    throw new UsuarioDuplicateException();
                }
            }
            
            // Actualizar los campos del usuario
            userToUpdate.updateFromDTO(usuario);
            return usuarioRepository.save(userToUpdate);
        }
        
        return null;
    }


//validamos por email en lugar de nombre, ya que es más común para usuarios.
// by kiki
    @Override
    @Transactional // este transactional asegura que si algo falla, no se guarde nada en la base de datos.
    public Usuario createUsuario(UsuarioDTO usuario) throws UsuarioDuplicateException {
    List<Usuario> usuarios = usuarioRepository.findByEmail(usuario.getEmail());
    Rol rol = rolRepository.findById(usuario.getRol().getId())
        .orElseThrow(() -> new RuntimeException("Rol not found"));
    usuario.setRol(rol.toDTO());
    if (usuarios.isEmpty()) {
        return usuarioRepository.save(
            new Usuario(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getDireccion(),
                rol // <-- Aquí pasas el objeto persistido
            )
        );
    } else {
        throw new UsuarioDuplicateException();
    }
}
}
