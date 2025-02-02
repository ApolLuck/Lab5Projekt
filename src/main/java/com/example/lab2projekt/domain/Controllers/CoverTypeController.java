package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Services.CoverTypeService;
import com.example.lab2projekt.domain.Services.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CoverTypeController {
    private final CoverTypeService coverTypeService;
    private final PizzaService pizzaService;

    public CoverTypeController(CoverTypeService coverTypeService, PizzaService pizzaService) {
        this.coverTypeService = coverTypeService;
        this.pizzaService = pizzaService;
    }

    // Modyfikacja nazwy opakowania
    @GetMapping("/newCoverTypeName")
    public String updateCoverTypeName(
            @RequestParam("nowaNazwa") String nowaNazwa,
            @RequestParam("staraNazwa") String staraNazwa,
            Model model)
    {
        coverTypeService.updateCoverTypeName(nowaNazwa, staraNazwa);
        List<Pizza> pizzas = pizzaService.findAllPizzas();
        model.addAttribute("pizze", pizzas);
        return "redirect:pizzas";
    }
}
