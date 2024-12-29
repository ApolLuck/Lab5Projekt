package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.User.AppUser;
import com.example.lab2projekt.domain.Objects.User.Role;
import com.example.lab2projekt.domain.repositories.RoleRepository;
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
    private RoleRepository roleRepository;

    @Autowired
    public UserRegistrationService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // Rejestracja użytkownika
    public void registerUser(AppUser appUser) throws MessagingException {
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);  // Ustawienie zakodowanego hasła
        appUser.setActivationCode(generateActivationCode());
        userRepository.save(appUser);
        emailService.sendActivationEmail(appUser.getEmail(), appUser.getActivationCode());
    }

    // Aktywacja użytkownika na podstawie kodu aktywacyjnego
    public boolean activateUser(String activationCode) {
        Optional<AppUser> userOpt = userRepository.findByActivationCode(activationCode);

        if (userOpt.isPresent()) {
            AppUser appUser = userOpt.get();
            appUser.setActivationCode(null); // Ustawienie kodu aktywacyjnego na null po aktywacji
            appUser.setEnabled(true); // Aktywowanie konta

            // Pobierz rolę ROLE_USER z bazy danych
            Optional<Role> roleOpt = roleRepository.findByType(Role.Types.ROLE_USER);
            if (roleOpt.isPresent()) {
                Role roleUser = roleOpt.get();
                appUser.getRoles().add(roleUser);
            } else {
                throw new RuntimeException("Role ROLE_USER not found in the database");
            }
            userRepository.save(appUser);

            return true;
        }
        return false;
    }

    // Generowanie unikalnego kodu aktywacyjnego
    private String generateActivationCode() {
        return java.util.UUID.randomUUID().toString(); // Użycie UUID jako kodu aktywacyjnego
    }
}
