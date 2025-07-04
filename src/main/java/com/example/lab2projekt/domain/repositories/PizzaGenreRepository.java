package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.PizzaGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaGenreRepository extends JpaRepository<PizzaGenre, Integer> {
}
