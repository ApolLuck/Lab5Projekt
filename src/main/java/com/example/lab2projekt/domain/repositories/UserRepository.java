package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.User.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByActivationCode(String activationCode);
}
