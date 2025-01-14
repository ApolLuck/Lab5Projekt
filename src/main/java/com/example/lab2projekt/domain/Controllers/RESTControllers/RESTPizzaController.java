package com.example.lab2projekt.domain.Controllers.RESTControllers;

import com.example.lab2projekt.domain.Objects.Pizza;
import com.example.lab2projekt.domain.Services.PizzaService;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class RESTPizzaController {
    private final PizzaService pizzaService;

    public RESTPizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/api/pizza/details")
    public ResponseEntity<Pizza> showDetails(@RequestParam(value = "id", required = false, defaultValue = "-1") Integer pizzaId) {
        Optional<Pizza> pizzaOptional = pizzaService.findPizzaById(pizzaId);
        if (pizzaOptional.isPresent()) {
            Pizza pizza = pizzaOptional.get();
            // Zastąp odwrotne ukośniki na normalne ukośniki
            String normalizedFileName = pizza.getFileName().replace("\\", "/");
            String relativeFilePath = normalizedFileName.replace("D:/polak/pizza/", ""); // Usuwamy początkową część ścieżki
            pizza.setFileName(relativeFilePath); // Ustawiamy przetworzoną ścieżkę
            return ResponseEntity.ok(pizza); // Zwracamy pizzę w odpowiedzi
        } else {
            return ResponseEntity.notFound().build(); // Jeśli nie znaleziono, zwracamy 404
        }
    }

    @GetMapping("/api/deletePizza/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)//204 No Content
    public String deletePizza(@PathVariable Integer id) {
        try {
            pizzaService.deletePizza(id);
        } catch (JDBCConnectionException ex) {
            throw ex;
        }
        return "redirect:/pizzas";
    }

}
