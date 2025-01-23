package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.User.Role;
import com.example.lab2projekt.domain.repositories.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono roli o ID: " + id));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }
}
