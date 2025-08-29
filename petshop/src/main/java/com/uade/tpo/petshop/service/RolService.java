package com.uade.tpo.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.Rol;
import com.uade.tpo.petshop.entity.dtos.RolDTO;
import com.uade.tpo.petshop.entity.exceptions.RolDuplicateException;
import com.uade.tpo.petshop.repositories.interfaces.IRolRepository;
import com.uade.tpo.petshop.service.interfaces.IRolService;


/*
 * los metodos del repository son los incluidos en el crud de JPA
 * salvo por los personalizados agregados en la interfaz del repository
 * @fedeb123
 */

@Service
public class RolService implements IRolService {
    private final IRolRepository rolRepository;

    public RolService(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Page<Rol> getAllRoles(PageRequest pageable) {
        return rolRepository.findAll(pageable);
    }

    @Override
    public Optional<Rol> getRolById(Long rolId) {
        return rolRepository.findById(rolId);
    }

    @Override
    public Rol createRol(RolDTO rol) throws RolDuplicateException {
        // Implementacion del metodo para crear un nuevo Rol
        List<Rol> roles = rolRepository.findByName(rol.getNombre());
        if(roles.isEmpty()){
            return rolRepository.save(new Rol(rol.getNombre()));
        } else {
            throw new RolDuplicateException();
        }
    }
}
