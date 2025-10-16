package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Rol;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.UsuarioDTO;
import com.uade.tpo.petshop.entity.dtos.UsuarioPersonalDataDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.repositories.interfaces.IUsuarioRepository;
import com.uade.tpo.petshop.service.interfaces.IRolService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IRolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<Usuario> getAllUsuarios(PageRequest pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId);

    }

    @Override
    @Transactional
    public UsuarioDTO getUsuarioConTodoPorId(Long id) throws MissingUserException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(MissingUserException::new);

        usuario.getPedidos().size();
        usuario.getProductos_creados().size();

        return usuario.toDTO();
    }

    @Override
    public UsuarioPersonalDataDTO getUsuarioPersonalDataByEmail(String email) throws MissingUserException{
        Usuario usuario = usuarioRepository.findByEmailPersonalData(email)
            .orElseThrow(MissingUserException::new);
    
        return new UsuarioPersonalDataDTO(usuario.getId(), usuario.getNombre(),usuario.getApellido(),usuario.getTelefono(),usuario.getEmail(),usuario.getDireccion());
    }

//comentario a discusion: ver si nos manejamos por email o por id

    @Override
    public Optional<Usuario> getUsuarioByEmail(String email) {
        List<Usuario> usuarios = usuarioRepository.findByEmail(email);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }

    @Override
    @Transactional
    public void deleteUsuarioById(Long id) throws MissingUserException {
        this.getUsuarioById(id).orElseThrow(() -> new MissingUserException());
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario updateUsuario(Long id, UsuarioDTO usuario) throws UsuarioDuplicateException, MissingUserException {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new MissingUserException());        

        // Verificar si el nuevo email ya existe en otro usuario
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            List<Usuario> usuariosConEmail = usuarioRepository.findByEmail(usuario.getEmail());
            if (!usuariosConEmail.isEmpty()) {
                throw new UsuarioDuplicateException();
            }
        }
        
        // Actualizar los campos del usuario
        usuarioExistente.updateFromDTO(usuario);
        // Si la contraseña fue cambiada, la encriptamos
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuarioExistente);
        
    }


//validamos por email en lugar de nombre, ya que es más común para usuarios.
// by kiki
    @Override
    @Transactional // este transactional asegura que si algo falla, no se guarde nada en la base de datos.
    public Usuario createUsuario(UsuarioDTO usuario) throws UsuarioDuplicateException, MissingRolException {
        Rol rol = rolService.getRolById(usuario.getRolId()).orElseThrow(() -> new MissingRolException());
        List<Usuario> usuarios = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarios.isEmpty()) {
            return usuarioRepository.save(new Usuario(usuario.getNombre(), usuario.getApellido(), usuario.getTelefono(), usuario.getEmail(), usuario.getPassword(), usuario.getDireccion(), rol));
        } else {
            throw new UsuarioDuplicateException();
        }
    }
}
