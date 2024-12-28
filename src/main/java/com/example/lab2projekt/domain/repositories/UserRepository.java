package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByActivationCode(String activationCode);
}
