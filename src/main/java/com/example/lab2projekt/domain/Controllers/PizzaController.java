package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Exceptions.PizzaNotFoundException;
import com.example.lab2projekt.domain.Objects.*;
import com.example.lab2projekt.domain.Services.CoverTypeService;
import com.example.lab2projekt.domain.Services.PizzaGenreService;
import com.example.lab2projekt.domain.Services.PizzaService;
import com.example.lab2projekt.domain.repositories.PizzaGenreRepository;
import com.example.lab2projekt.domain.repositories.PizzaRepository;
import com.example.lab2projekt.domain.repositories.CoverTypeRepository;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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

        String searchPharse = "%" + pharse + "%";
        List<Pizza> pizzas = pizzaService.findPizzasByPhrase(searchPharse);
        model.addAttribute("pizzas", pizzas);
        return "pizzaList";
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

    // Wyszukiwanie pizzy na podstawie parametrów
    @GetMapping("/list")
    public String filterPizzas(
            @RequestParam(required = false) String nazwa,
            @RequestParam(required = false) Float minCena,
            @RequestParam(required = false) Float maxCena,
            @RequestParam(required = false) Integer coverTypeId,
            Model model) {

        List<Pizza> pizzas;

        // Wyszukiwanie po nazwie
        if (nazwa != null && !nazwa.isEmpty()) {
            pizzas = pizzaService.findByNazwaContainingIgnoreCaseOrSkladnikiContainingIgnoreCase(nazwa, nazwa);
        }
        //Wyszukiwanie po zakresie cen
        else if (minCena != null && maxCena != null) {
            pizzas = pizzaService.findByCenaBetween(minCena, maxCena);
        }
        //Wyszukiwanie po coverType
        else if (coverTypeId != null) {
            CoverType coverType = coverTypeService.findById(coverTypeId)
                    .orElse(null);
            pizzas = pizzaService.findByCoverType(coverType);
        } else {
            pizzas = pizzaService.findAllPizzas();
        }

        model.addAttribute("pizzas", pizzas);
        return "pizzaList";
    }

    //  Wyswietlanie wszystkich pizz
    @GetMapping("/pizzas")
    public String showPizza(Model model) {
        List<Pizza> pizzas = pizzaService.findAllPizzas();
        pizzas.forEach(pizza -> {
            if (pizza.getFileContent() != null) {
                String base64Content = Base64.getEncoder().encodeToString(pizza.getFileContent());
                pizza.setFileName(base64Content); // Tymczasowe pole na zakodowany obraz
            }
        });
        model.addAttribute("pizze", pizzas);
        log.log(Level.DEBUG, "komunikat z metody showPizza");
        return "showMenu";
    }

    // Wyswietlanie pojedynczej pizzy
    @GetMapping("/pizza")
    public ModelAndView pizza(@RequestParam(value = "id") Integer pizzaId) {
        ModelAndView mav = new ModelAndView("showOnePizza");
        try {
            Optional<Pizza> onePizza = pizzaService.findPizzaById(pizzaId);
            if (onePizza.isEmpty()) {
                throw new PizzaNotFoundException("Pizza o id " + pizzaId + " nie została znaleziona");
            }
            onePizza.ifPresent(pizza -> mav.addObject("pizzunia", pizza));
        } catch (PizzaNotFoundException ex) {
            throw ex;
        } catch (JDBCConnectionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Wystąpił nieoczekiwany błąd podczas pobierania pizzy", ex);
        }
        return mav;
    }


    // Formularz edycji
    @GetMapping("/editPizza")
    public String showForm(Model model, @RequestParam(value = "id", required = false, defaultValue = "-1") Integer pizzaId) {
        Pizza pizza = pizzaService.findPizzaById(pizzaId).orElse(new Pizza());
        model.addAttribute("pizza", pizza);
        log.log(Level.DEBUG, "komunikat z metody showForm");
        return "editForm";
    }

    // Update pizzy
    @PostMapping("/updatePizza")
    public String processForm(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result,
                              MultipartFile multipartFile) {
        try {
            if (result.hasErrors()) {
                return "editForm";
            }
            if(!multipartFile.isEmpty()) {//to powinno być w usłudze
                pizza.setFileName(multipartFile.getOriginalFilename());
                pizza.setFileContent(multipartFile.getBytes());
            }

            pizzaService.savePizza(pizza);
            log.log(Level.DEBUG, "komunikat z metody processForm");
        } catch (JDBCConnectionException ex) {
            throw ex;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/pizzas";
    }

    // Dodanie nowej pizzy
    @GetMapping("/addPizza")
    public String showAddPizzaForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        log.log(Level.DEBUG, "komunikat z metody showAddPizzaForm");
        return "editForm";
    }

    @PostMapping("/addPizza")
    public String addPizza(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result) {
        if (result.hasErrors()) {
            return "editForm";
        }
        pizzaService.savePizza(pizza);
        log.log(Level.DEBUG, "komunikat z metody AddPizza");
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
        pizzaService.findPizzaById(pizzaId).ifPresent(pizza -> mav.addObject("pizzunia", pizza));
        log.log(Level.DEBUG, "komunikat z metody pizzaDetails");
        return mav;
    }

    @InitBinder("pizza")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new CustomPizzaValidator());
        binder.registerCustomEditor(FormatPizzy.class, new PizzaPropertyEditor());
        log.log(Level.DEBUG, "komunikat z metody initBinder");
    }

}
