package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Services.PizzaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SearchController {
    private final PizzaService pizzaService;

    public SearchController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("/filteredBySpecification")
    public String filterPizzasBySpecification(
            @RequestParam(required = false) String pharse,
            @RequestParam(required = false) Double cenaOd,
            @RequestParam(required = false) Double cenaDo,
            @RequestParam(required = false) LocalDate dataOd,
            @RequestParam(required = false) LocalDate dataDo,
            Model model) {

        List<Pizza> pizzas = pizzaService.findPizzasByFilter(pharse, cenaOd, cenaDo, dataOd, dataDo);
        model.addAttribute("pizzas", pizzas);
        return "pizzaList";
    }


    // Wyszukiwanie pizzy na podstawie obiektu filtra
    @GetMapping("/filtered")
    public String filterPizzasbyFilterObject(
            @RequestParam(required = false) String pharse,
            @RequestParam(required = false) Double cenaOd,
            @RequestParam(required = false) Double cenaDo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataOd,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataDo,
            Model model) {

        List<Pizza> pizzas = pizzaService.findPizzasByFilter(pharse, cenaOd, cenaDo, dataOd, dataDo);
        model.addAttribute("pizzas", pizzas);
        return "pizzaList";
    }


    //Wyszukiwanie pizzy na podstawie frazy
    @GetMapping("/pharse")
    public String pharsePizzas(
            @RequestParam("pharse")
            String pharse,
            Model model) {

        List<Pizza> pizzas = pizzaService.findPizzasByPhrase(pharse);
        model.addAttribute("pizzas", pizzas);
        return "pizzaList";
    }

    // Wyszukiwanie pizzy na podstawie parametrów
    @GetMapping("/list")
    public String filterPizzas(
            @RequestParam(required = false) String nazwa,
            @RequestParam(required = false) Float minCena,
            @RequestParam(required = false) Float maxCena,
            @RequestParam(required = false) Integer coverTypeId,
            Model model) {

        List<Pizza> pizzas = pizzaService.filterPizzas(nazwa, minCena, maxCena, coverTypeId);

        model.addAttribute("pizzas", pizzas);
        return "pizzaList";
    }
}
