package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.*;
import com.example.lab2projekt.domain.Objects.Entities.*;
import com.example.lab2projekt.domain.Objects.Formatters.FormatPizzy;
import com.example.lab2projekt.domain.Objects.Validators.CustomPizzaValidator;
import com.example.lab2projekt.domain.Services.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Controller
@Log4j2
public class PizzaController<JBClass> {
    private final PizzaService pizzaService;
    private final CoverTypeService coverTypeService;
    private final PizzaGenreService pizzaGenreService;

    @Autowired
    public PizzaController(PizzaService pizzaService, CoverTypeService coverTypeService, PizzaGenreService pizzaGenreService) {
        this.pizzaService = pizzaService;
        this.coverTypeService = coverTypeService;
        this.pizzaGenreService = pizzaGenreService;
    }

    @ModelAttribute("coverTypes")
    public List<CoverType> getCoverTypes() {
        return coverTypeService.findAllCoverTypes();
    }

    @ModelAttribute("pizzaGenres")
    public List<PizzaGenre> getPizzaGenres() {
        return pizzaGenreService.findAllGenres();
    }

    @ModelAttribute("pizzaSizes")
    public List<PizzaSize> getPizzaSizes() {
        return List.of(PizzaSize.values());
    }

    //  Wyswietlanie wszystkich pizz
    @GetMapping("/pizzas")
    public String showPizza(Model model) {
        List<Pizza> pizzas = pizzaService.findAllPizzas();

        pizzas.forEach(pizza -> {
            if (pizza.getFileContent() != null) {
                String base64Content = Base64.getEncoder().encodeToString(pizza.getFileContent());
                pizza.setFileName(base64Content);
            }
        });
        model.addAttribute("pizze", pizzas);
        return "showMenu";
    }

    //wyswietlanie menu klienta
    @GetMapping("/menu")
    public String showmenu(Model model) {
        List<Pizza> pizzas = pizzaService.findAllPizzas();
        pizzas.forEach(pizza -> {
            if (pizza.getFileContent() != null) {
                String base64Content = Base64.getEncoder().encodeToString(pizza.getFileContent());
                pizza.setFileName(base64Content);
            }
        });
        model.addAttribute("pizze", pizzas);
        return "menu";
    }

    // wyswietlanie galerii dla klienta
    @GetMapping("/gallery")
    public String showGallery(Model model) {
        List<Pizza> pizzas = pizzaService.findAllPizzas();
        pizzas.forEach(pizza -> {
            if (pizza.getFileContent() != null) {
                String base64Content = Base64.getEncoder().encodeToString(pizza.getFileContent());
                pizza.setFileName(base64Content);
            }
        });
        model.addAttribute("pizze", pizzas);
        return "gallery";
    }


    // Wyswietlanie pojedynczej pizzy
    @GetMapping("/pizza")
    public ModelAndView pizza(@RequestParam(value = "id") Integer pizzaId) {
        ModelAndView mav = new ModelAndView("showOnePizza");
        Pizza pizza = pizzaService.getPizzaById(pizzaId);
        mav.addObject("pizzunia", pizza);
        return mav;
    }

    // Formularz edycji
    @GetMapping("/editPizza")
    public String showForm(Model model, @RequestParam(value = "id", required = false, defaultValue = "-1") Integer pizzaId) {
        Pizza pizza = pizzaService.findPizzaById(pizzaId).orElse(new Pizza());
        model.addAttribute("pizza", pizza);
        String formattedDate = pizza.getData_wprowadzenia().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("formattedDate", formattedDate);
        return "editForm";
    }

    // Dodanie nowej pizzy
    @GetMapping("/addPizza")
    public String showAddPizzaForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "editForm";
    }

    @PostMapping("/addPizza")
    public String addPizza(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("coverTypes", coverTypeService.findAllCoverTypes());
            model.addAttribute("pizzaGenres", pizzaGenreService.findAllGenres());
            return "editForm";
        }
        pizzaService.savePizza(pizza);
        return "redirect:/pizzas";
    }

    @GetMapping("/deletePizza")
    public String deletePizza(@RequestParam("id") Integer id) {
        try {
            pizzaService.deletePizza(id);
        } catch (JDBCConnectionException ex) {
            throw ex;
        }
        return "redirect:/pizzas";
    }

    // Wyswietlanie pizzy na podstawie id
    @GetMapping("/details")
    public ModelAndView pizzaDetails(@RequestParam(value = "id", required = false, defaultValue = "-1") Integer pizzaId) {
        ModelAndView mav = new ModelAndView("showOnePizza");

        pizzaService.findPizzaById(pizzaId).ifPresent(pizza -> {
            if (pizza.getFileContent() != null) {
                String base64Content = Base64.getEncoder().encodeToString(pizza.getFileContent());
                pizza.setFileName(base64Content);
            }
            mav.addObject("pizzunia", pizza);
        });
        return mav;
    }

    @GetMapping("/addPizzaToBasket")
    public String addPizzaToBasket(Model model, @RequestParam(value = "id", defaultValue = "1") Integer pizzaId) {
        Pizza pizza = pizzaService.findPizzaById(pizzaId).orElse(new Pizza());
        model.addAttribute("pizza", pizza);
        return "choosenPizza";
    }

    @InitBinder("pizza")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new CustomPizzaValidator());
        binder.registerCustomEditor(FormatPizzy.class, new PizzaPropertyEditor());
    }

}
