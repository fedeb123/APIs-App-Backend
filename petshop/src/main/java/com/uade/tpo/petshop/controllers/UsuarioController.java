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
import com.uade.tpo.petshop.entity.exceptions.UsuarioDuplicateException;
import com.uade.tpo.petshop.service.UsuarioService;
import com.uade.tpo.petshop.service.interfaces.IUsuarioService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

	@Autowired
	private final IUsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService){
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<Page<Usuario>> getAllUsuarios(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
		if (page == null && size == null){
			return ResponseEntity.ok(usuarioService.getAllUsuarios(PageRequest.of(0, Integer.MAX_VALUE)));
		} else {
			return ResponseEntity.ok(usuarioService.getAllUsuarios(PageRequest.of(page, size)));
		}
	}

	@GetMapping("/{usuarioId}")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long usuarioId) {
		Optional<Usuario> usuario = usuarioService.getUsuarioById(usuarioId);
		if (usuario.isPresent()){
			return ResponseEntity.ok(usuario.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Object> createUsuario(@RequestBody UsuarioDTO usuario) throws UsuarioDuplicateException {
		Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
		return ResponseEntity.created(URI.create("/usuarios/" + nuevoUsuario.getId())).body(nuevoUsuario);
	}

	@DeleteMapping("/{usuarioId}")
	public void deleteUsuario(@PathVariable Long usuarioId) {
		usuarioService.deleteUsuarioById(usuarioId);
	}
}
