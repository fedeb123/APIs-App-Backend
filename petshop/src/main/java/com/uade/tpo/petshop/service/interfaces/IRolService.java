package com.uade.tpo.petshop.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.petshop.entity.Rol;
import com.uade.tpo.petshop.entity.dtos.RolDTO;
import com.uade.tpo.petshop.entity.exceptions.RolDuplicateException;

public interface IRolService {
    public Optional<Rol> getRolById(Long id);
    public Page<Rol> getAllRoles(PageRequest pageable);
    public Rol createRol(RolDTO rol) throws RolDuplicateException;    
}
