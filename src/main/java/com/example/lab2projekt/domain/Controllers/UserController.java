package com.example.lab2projekt.domain.Controllers;

import com.example.lab2projekt.domain.Objects.User.User;
import com.example.lab2projekt.domain.Services.UserRegistrationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registerForm"; // Formularz rejestracji
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userRegistrationService.registerUser(user);
            model.addAttribute("message", "Rejestracja przebiegła pomyślnie! Sprawdź swoją skrzynkę e-mail.");
        } catch (MessagingException e) {
            model.addAttribute("error", "Wystąpił problem z wysłaniem e-maila.");
        }
        return "loginForm"; // Przekierowanie do formularza logowania
    }

    @GetMapping("/activate")
    public String activateUser(@RequestParam("code") String activationCode, Model model) {
        boolean isActivated = userRegistrationService.activateUser(activationCode);

        if (isActivated) {
            model.addAttribute("message", "Konto zostało aktywowane. Możesz się teraz zalogować.");
        } else {
            model.addAttribute("error", "Nie udało się aktywować konta. Kod aktywacyjny jest nieprawidłowy.");
        }

        return "loginForm"; // Strona logowania po aktywacji
    }

}
