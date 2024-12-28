package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.User.Role;
import com.example.lab2projekt.domain.Objects.User.User;
import com.example.lab2projekt.domain.repositories.RoleRepository;
import com.example.lab2projekt.domain.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserRegistrationService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

            // Pobierz rolę ROLE_USER z bazy danych
            Optional<Role> roleOpt = roleRepository.findByType(Role.Types.ROLE_USER);
            if (roleOpt.isPresent()) {
                Role roleUser = roleOpt.get();
                user.getRoles().add(roleUser);
            } else {
                throw new RuntimeException("Role ROLE_USER not found in the database");
            }
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
