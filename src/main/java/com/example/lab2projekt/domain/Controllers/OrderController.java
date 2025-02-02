package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.Entities.Order;
import com.example.lab2projekt.domain.Objects.Entities.OrderItem;
import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Services.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderItemService orderItemService;
    private final CookiesService cookiesService;
    private final OrderService orderService;
    private final AddressService addressService;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final PizzaService pizzaService;
    private final PromotionService promotionService;

    public OrderController(OrderItemService orderItemService, CookiesService cookiesService, OrderService orderService,
                           AddressService addressService, PaymentService paymentService, EmailService emailService,
                           PizzaService pizzaService, PromotionService promotionService) {
        this.orderItemService = orderItemService;
        this.cookiesService = cookiesService;
        this.orderService = orderService;
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.pizzaService = pizzaService;
        this.promotionService = promotionService;
    }

    // wyswietlanie koszyka dla klienta
    @GetMapping("/basket")
    public String showBasket(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<OrderItem> orderItems = orderItemService.findAllOrderItems();
        // Pobierz identyfikator użytkownika z ciasteczka
        String userCookieId = cookiesService.getCookiesForUser(request,response);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("UserCookies", userCookieId);
        model.addAttribute("promotions", promotionService.findAllPromotions());
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
        model.addAttribute("promotions", promotionService.findAllPromotions());
        return "basket";
    }

    @PostMapping("/createOrder")
    public String createOrder(
            @RequestParam("totalOrderValue") String totalOrderValue,
            @RequestParam Map<String, String> params,
            @RequestParam("userCookies") String userCookies,
            Model model) {

        orderService.processOrderCreation(params, totalOrderValue, userCookies);
        List<OrderItem> orderItems = orderItemService.findAllOrderItems();
        System.out.println("Przesszło");
        Order order = orderService.getOrderByCookie(userCookies);

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("Order", order);

        return "createOrder";
    }

    @PostMapping("/submit-order")
    public String confirmOrder(
            @RequestParam Map<String, String> params,
            @RequestParam ("OrderId") Order order,
            Model model
    ) {

        addressService.createAddress(params, order);
        orderService.updateOrderEmail(params, order);
        String userEmail = orderService.getEmail(params);

        model.addAttribute("OrderId", order.getId());
        model.addAttribute("userEmail", userEmail);

        return "payment";
    }

    @PostMapping("/processPayment")
    public String paymentProcess(
            @RequestParam ("creditCardNumber") String creditCardNumber,
            @RequestParam ("OrderId") Order order,
            @RequestParam ("userEmail") String userEmail,
            Model model
    ) {

        paymentService.createPayment(creditCardNumber, order);

        try {
            emailService.sendConfirmedEmail(userEmail, order);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        List<Pizza> pizze = pizzaService.findAllPizzas();
        model.addAttribute("pizze", pizze);


        return "redirect:menu";
    }

    @GetMapping("/yourOrder")
    public String searchOrders(
            @RequestParam(name = "clientEmail", required = false, defaultValue = "") String clientEmail,
            @RequestParam(name = "orderId", required = false) Long orderId,
            Model model) {

        List<Order> orders = orderService.searchOrders(clientEmail, orderId);
        model.addAttribute("orders", orders);
        return "yourOrder";
    }
}
