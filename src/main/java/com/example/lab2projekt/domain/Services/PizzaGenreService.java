package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.PizzaGenre;
import com.example.lab2projekt.domain.repositories.PizzaGenreRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaGenreService {

    private final PizzaGenreRepository pizzaGenreRepository;

    public PizzaGenreService(PizzaGenreRepository pizzaGenreRepository) {
        this.pizzaGenreRepository = pizzaGenreRepository;
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<PizzaGenre> findAllGenres() {
        return pizzaGenreRepository.findAll();
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public PizzaGenre findGenreById(Integer id) {
        return pizzaGenreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono gatunku o ID: " + id));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public PizzaGenre saveGenre(PizzaGenre genre) {
        return pizzaGenreRepository.save(genre);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public void deleteGenre(Integer id) {
        pizzaGenreRepository.deleteById(id);
    }
}
