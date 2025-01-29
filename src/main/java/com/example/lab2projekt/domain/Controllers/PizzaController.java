package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.*;
import com.example.lab2projekt.domain.Objects.Entities.*;
import com.example.lab2projekt.domain.Objects.Formatters.FormatPizzy;
import com.example.lab2projekt.domain.Objects.Validators.CustomPizzaValidator;
import com.example.lab2projekt.domain.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
public class PizzaController<JBClass> {

    private final PizzaService pizzaService;
    private final CoverTypeService coverTypeService;
    private final PizzaGenreService pizzaGenreService;
    private final FileService fileService;
    private final PromotionService promotionService;
    private final OrderItemService orderItemService;
    private final CookiesService cookiesService;
    private final OrderService orderService;

    @Autowired
    public PizzaController(PizzaService pizzaService, CoverTypeService coverTypeService, PizzaGenreService pizzaGenreService,
                           FileService fileService, PromotionService promotionService, OrderItemService orderItemService, CookiesService cookiesService, OrderService orderService) {
        this.pizzaService = pizzaService;
        this.coverTypeService = coverTypeService;
        this.pizzaGenreService = pizzaGenreService;
        this.fileService = fileService;
        this.promotionService = promotionService;
        this.orderItemService = orderItemService;
        this.cookiesService = cookiesService;
        this.orderService = orderService;
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
   // model.addAttribute("sizes", PizzaSize.values());

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

        List<Pizza> pizzas = pizzaService.filterPizzas(nazwa, minCena, maxCena, coverTypeId);

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

    // wyswietlanie promocji dla klienta
    @GetMapping("/promotions")
    public String showPromotions(Model model) {
        List<Promotion> promotions = promotionService.findAllPromotions();
        model.addAttribute("promocje", promotions);
        return "promotions";
    }

    // wyswietlanie koszyka dla klienta
    @GetMapping("/basket")
    public String showBasket(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<OrderItem> orderItems = orderItemService.findAllOrderItems();
        // Pobierz identyfikator użytkownika z ciasteczka
        String userCookieId = cookiesService.getCookiesForUser(request,response);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("UserCookies", userCookieId);
        return "basket";
    }

    @PostMapping("/basket")
    public String addToBasket(@RequestParam(value = "pizzaID", required = false) Integer pizzaId ,
                              @RequestParam(value = "quantity", required = false) Integer quantity,
                              @RequestParam(value = "orderValue", required = false) BigDecimal orderValue,
                              HttpServletRequest request, HttpServletResponse response, Model model) {

        // Pobierz identyfikator użytkownika z ciasteczka
        String userCookieId = cookiesService.getCookiesForUser(request,response);
        if (!(quantity ==null)){
            orderItemService.createOrderItem(quantity, orderValue, pizzaId, userCookieId);
        }
        List<OrderItem> orderItems = orderItemService.findAllOrderItems();
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("UserCookies", userCookieId);
        return "basket";
    }

    @PostMapping("/createOrder")
    public String createOrder(
            @RequestParam("totalOrderValue") String totalOrderValue,
            @RequestParam Map<String, String> params,
            @RequestParam("userCookies") String userCookies
    ) {
        // Wyświetlamy sumaryczną wartość zamówienia
//        System.out.println("Łączna wartość zamówienia: " + totalOrderValue);

        orderService.processOrderCreation(params, totalOrderValue, userCookies);

//        // Iterujemy po tablicy items
//        params.forEach((key, value) -> {
//            if (key.startsWith("items")) {
//                System.out.println("Parametr: " + key + " = " + value);
//            }
//        });
        return "createOrder";
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

    // Update pizzy
    @PostMapping("/updatePizza")
    public String processForm(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result,
                              MultipartFile multipartFile) {
        try {
            if (result.hasErrors()) {
                return "editForm";
            }
            if(!multipartFile.isEmpty()) {//to powinno być w usłudze
                pizza.setFileName(multipartFile.getOriginalFilename());    //wersja z BD w h2
                pizza.setFileContent(multipartFile.getBytes());           // wersja z BD w h2
                //String savedFilePath = fileService.saveFile(multipartFile, "pizza", pizza.getId().toString());
               // pizza.setFileName(savedFilePath); // Zapisujemy ścieżkę w encji
            }

            pizzaService.savePizza(pizza);
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
        return "editForm";
    }

    @PostMapping("/addPizza")
    public String addPizza(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult result) {
        if (result.hasErrors()) {
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
            // Zastąp odwrotne ukośniki na normalne ukośniki
            if (pizza.getFileContent() != null) {
                String base64Content = Base64.getEncoder().encodeToString(pizza.getFileContent());
                pizza.setFileName(base64Content); // Tymczasowe pole na zakodowany obraz
            }
            mav.addObject("pizzunia", pizza);
        });
        return mav;
    }

    @GetMapping("/addPizzaToBasket")
    public String addPizzaToBasket(Model model, @RequestParam(value = "id", required = false, defaultValue = "-1") Integer pizzaId) {
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
