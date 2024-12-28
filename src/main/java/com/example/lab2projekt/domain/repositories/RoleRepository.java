package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
     Optional<Role> findByType(Role.Types type);
}
