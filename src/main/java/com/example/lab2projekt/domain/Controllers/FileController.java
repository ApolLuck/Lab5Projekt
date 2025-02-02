package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Services.CoverTypeService;
import com.example.lab2projekt.domain.Services.PizzaGenreService;
import com.example.lab2projekt.domain.Services.PizzaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Controller
public class FileController {
    private final PizzaService pizzaService;
    private final CoverTypeService coverTypeService;
    private final PizzaGenreService pizzaGenreService;

    public FileController(PizzaService pizzaService, CoverTypeService coverTypeService, PizzaGenreService pizzaGenreService) {
        this.pizzaService = pizzaService;
        this.coverTypeService = coverTypeService;
        this.pizzaGenreService = pizzaGenreService;
    }

    @PostMapping("/updatePizza")
    public String processForm(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result,
                              MultipartFile multipartFile, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("coverTypes", coverTypeService.findAllCoverTypes());
                model.addAttribute("pizzaGenres", pizzaGenreService.findAllGenres());
                return "editForm";
            }
            pizzaService.addFile(multipartFile, pizza);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/pizzas";
    }

}
