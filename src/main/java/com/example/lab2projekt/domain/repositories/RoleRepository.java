package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
