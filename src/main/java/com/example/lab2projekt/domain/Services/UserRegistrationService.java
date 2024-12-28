package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.User.User;
import com.example.lab2projekt.domain.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // Rejestracja użytkownika
    public void registerUser(User user) throws MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);  // Ustawienie zakodowanego hasła
        user.setActivationCode(generateActivationCode());
        userRepository.save(user);
        emailService.sendActivationEmail(user.getEmail(), user.getActivationCode());
    }

    // Aktywacja użytkownika na podstawie kodu aktywacyjnego
    public boolean activateUser(String activationCode) {
        Optional<User> userOpt = userRepository.findByActivationCode(activationCode);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActivationCode(null); // Ustawienie kodu aktywacyjnego na null po aktywacji
            user.setEnabled(true); // Aktywowanie konta
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Generowanie unikalnego kodu aktywacyjnego
    private String generateActivationCode() {
        return java.util.UUID.randomUUID().toString(); // Użycie UUID jako kodu aktywacyjnego
    }
}
