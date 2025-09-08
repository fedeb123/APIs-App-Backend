package com.uade.tpo.petshop.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.UsuarioDTO;
import com.uade.tpo.petshop.entity.exceptions.MissingRolException;
import com.uade.tpo.petshop.entity.exceptions.MissingUserException;
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

	@Autowired
	private final IUsuarioService usuarioService;

	public UsuarioController(IUsuarioService usuarioService){
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<Page<UsuarioDTO>> getAllUsuarios(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
		Page<Usuario> usuarios;
		if (page == null && size == null){
			usuarios = usuarioService.getAllUsuarios(PageRequest.of(0, Integer.MAX_VALUE));
		} else {
			usuarios = usuarioService.getAllUsuarios(PageRequest.of(page, size));
		}
		Page<UsuarioDTO> usuariosDTO = usuarios.map(Usuario::toDTO);
		return ResponseEntity.ok(usuariosDTO);
	}

	@GetMapping("/{usuarioId}")
	public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long usuarioId) {
		Optional<Usuario> usuario = usuarioService.getUsuarioById(usuarioId);
		if (usuario.isPresent()){
			return ResponseEntity.ok(usuario.get().toDTO());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuario) throws UsuarioDuplicateException, MissingRolException {
		Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
		return ResponseEntity.created(URI.create("/usuarios/" + nuevoUsuario.getId())).body(nuevoUsuario.toDTO());
	}

	@DeleteMapping("/{usuarioId}")
	public void deleteUsuario(@PathVariable Long usuarioId) throws MissingUserException {
		usuarioService.deleteUsuarioById(usuarioId);
	}
}
