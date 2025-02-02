package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Promotion;
import com.example.lab2projekt.domain.Services.PizzaService;
import com.example.lab2projekt.domain.Services.PromotionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/promotions")
public class PromotionController {
    private final PromotionService promotionService;
    private final PizzaService pizzaService;

    public PromotionController(PromotionService promotionService, PizzaService pizzaService) {
        this.promotionService = promotionService;
        this.pizzaService = pizzaService;
    }

    @GetMapping("/clientPromotions")
    public String showPromotions(Model model) {
        List<Promotion> promotions = promotionService.findAllPromotions();
        model.addAttribute("promocje", promotions);
        return "clientPromotions";
    }

    @GetMapping
    public String listPromotions(Model model) {
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "promotions";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("pizzas", pizzaService.findAllPizzas());
        return "promotionForm";
    }

    @PostMapping("/save")
    public String savePromotion(@Valid @ModelAttribute("promotion") Promotion promotion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pizzas", pizzaService.findAllPizzas());
            return "promotionForm";
        }
        promotionService.savePromotion(promotion);
        return "redirect:/promotions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Promotion> promotion = promotionService.findPromotionById(id);
        if (promotion.isPresent()) {
            model.addAttribute("promotion", promotion.get());
            model.addAttribute("pizzas", pizzaService.findAllPizzas());
            return "promotionForm";
        }
        return "redirect:/promotions";
    }

    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Integer id) {
        promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }
}
