package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Services.PizzaService;
import jakarta.validation.Valid;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Controller
public class FileController {
    private final PizzaService pizzaService;

    public FileController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @PostMapping("/updatePizza")
    public String processForm(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result,
                              MultipartFile multipartFile) {
        try {
            if (result.hasErrors()) {
                return "editForm";
            }
            pizzaService.addFile(multipartFile, pizza);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/pizzas";
    }

}
