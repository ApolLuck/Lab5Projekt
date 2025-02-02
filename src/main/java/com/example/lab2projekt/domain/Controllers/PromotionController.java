package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Promotion;
import com.example.lab2projekt.domain.Services.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/promotions")
    public String showPromotions(Model model) {
        List<Promotion> promotions = promotionService.findAllPromotions();
        model.addAttribute("promocje", promotions);
        return "promotions";
    }
}
