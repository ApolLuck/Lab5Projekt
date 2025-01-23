package com.example.lab2projekt.domain.Config;

import com.example.lab2projekt.domain.Objects.Entities.CoverType;
import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Objects.Entities.PizzaGenre;
import com.example.lab2projekt.domain.Objects.User.Role;
import com.example.lab2projekt.domain.Objects.User.AppUser;
import com.example.lab2projekt.domain.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class RepositoryInitializer {
    private PizzaRepository pizzaRepository;
    private CoverTypeRepository coverTypeRepository;
    private PizzaGenreRepository pizzaGenreRepository;
    private  RoleRepository roleRepository;
    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;

    @Autowired
    public void setPizzaRepository(PizzaRepository pizzaRepository, CoverTypeRepository coverTypeRepository,
                                   PizzaGenreRepository pizzaGenreRepository, RoleRepository roleRepository,
                                   UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.pizzaRepository = pizzaRepository;
        this.coverTypeRepository = coverTypeRepository;
        this.pizzaGenreRepository = pizzaGenreRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    InitializingBean init() {
        return () -> {
            if (pizzaRepository.findAll().isEmpty()) {
                // Tworzenie i zapisywanie typów opakowań
                CoverType coverType1 = new CoverType("Standard", "Papier");
                coverTypeRepository.save(coverType1);

                CoverType coverType2 = new CoverType("Premium", "Plastik");
                coverTypeRepository.save(coverType2);

                CoverType coverType3 = new CoverType("Eko", "Bio");
                coverTypeRepository.save(coverType3);

                // Tworzenie i zapisywanie gatunków pizzy
                PizzaGenre pizzaGenre1 = new PizzaGenre("Bezglutenowa");
                pizzaGenreRepository.save(pizzaGenre1);

                PizzaGenre pizzaGenre2 = new PizzaGenre("Wegetariańska");
                pizzaGenreRepository.save(pizzaGenre2);

                PizzaGenre pizzaGenre3 = new PizzaGenre("Wegańska");
                pizzaGenreRepository.save(pizzaGenre3);

                // Tworzenie pizzy i przypisywanie odpowiednich gatunków
                Pizza pizza1 = new Pizza("Margherita", "Ser, sos pomidorowy", 31, 33f,
                        false, false, LocalDate.now(), coverType1);
                Set<PizzaGenre> genres1 = new HashSet<>();
                genres1.add(pizzaGenre1);
                genres1.add(pizzaGenre2);
                pizza1.setGenres(genres1);

                Pizza pizza2 = new Pizza("Pepperoni", "Ser, sos pomidorowy, pepperoni", 32,
                        34f, true, true, LocalDate.now(), coverType2);
                Set<PizzaGenre> genres2 = new HashSet<>();
                genres2.add(pizzaGenre1);
                genres2.add(pizzaGenre3);
                pizza2.setGenres(genres2);

                Pizza pizza3 = new Pizza("Hawaiian", "Ser, szynka, ananas", 33, 35f,
                        false, true, LocalDate.now(), coverType3);
                Set<PizzaGenre> genres3 = new HashSet<>();
                genres3.add(pizzaGenre2);
                pizza3.setGenres(genres3);

                pizzaRepository.save(pizza1);
                pizzaRepository.save(pizza2);
                pizzaRepository.save(pizza3);
            }
            if (roleRepository.findAll().isEmpty()) {
                Role roleUser = new Role(Role.Types.ROLE_USER);
                Role roleAdmin = new Role(Role.Types.ROLE_ADMIN);
                roleRepository.save(roleUser);
                roleRepository.save(roleAdmin);

                AppUser appUser = new AppUser("user", true);
                appUser.setRoles(Set.of(roleUser));
                appUser.setPassword(passwordEncoder.encode("user"));

                AppUser admin = new AppUser("admin", true);
                admin.setRoles(Set.of(roleAdmin));
                admin.setPassword(passwordEncoder.encode("admin"));

                AppUser test = new AppUser("test", true);
                test.setRoles(Set.of(roleUser, roleAdmin));
                test.setPassword(passwordEncoder.encode("test"));

                userRepository.save(appUser);
                userRepository.save(admin);
                userRepository.save(test);
            }
        };
    }
}
