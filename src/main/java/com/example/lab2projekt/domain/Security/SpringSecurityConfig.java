package com.example.lab2projekt.domain.Security;

import com.example.lab2projekt.domain.Services.ProfileNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import java.time.format.DateTimeFormatter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Profile(ProfileNames.USERS_IN_MEMORY)
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        var manager = new InMemoryUserDetailsManager();
        User.UserBuilder userBuilder = User.builder();

        var user = userBuilder
                .username("User")
                .password(passwordEncoder.encode("User"))
                .roles("USER")
                .build();
        manager.createUser(user);

        var admin = userBuilder
                .username("Admin")
                .password(passwordEncoder.encode("Admin"))
                .roles("ADMIN")
                .build();
        manager.createUser(admin);

        var superUser = userBuilder
                .username("SuperUser")
                .password(passwordEncoder.encode("SuperUser"))
                .roles("ADMIN", "USER")
                .build();
        manager.createUser(superUser);

        return manager;
    }


    @Component
    public class WebMvcConfigurerImpl implements WebMvcConfigurer {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/Zaloguj").setViewName("loginForm");
            registry.addViewController("/url_error403").setViewName("errors/error403");
        }

        @Override
        public void addFormatters(FormatterRegistry registry) {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            registrar.registerFormatters(registry);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/"),
                                mvcMatcherBuilder.pattern("/pizzas"),
                                mvcMatcherBuilder.pattern("/menu"),
                                mvcMatcherBuilder.pattern("/pizza/**"),
                                mvcMatcherBuilder.pattern("/Zaloguj"),
                                mvcMatcherBuilder.pattern("/register"),
                                mvcMatcherBuilder.pattern("/activate"),
                                mvcMatcherBuilder.pattern("/polak/**"),
                                mvcMatcherBuilder.pattern("/details"),
                                mvcMatcherBuilder.pattern("/gallery"),
                                mvcMatcherBuilder.pattern("/promotions/clientPromotions"),
                                mvcMatcherBuilder.pattern("/addPizzaToBasket"),
                                mvcMatcherBuilder.pattern("/createOrder"),
                                mvcMatcherBuilder.pattern("/basket"),
                                mvcMatcherBuilder.pattern("/yourOrder"),
                                mvcMatcherBuilder.pattern("/submit-order"),
                                mvcMatcherBuilder.pattern("/processPayment"),
                                mvcMatcherBuilder.pattern("/css/**"),
                                mvcMatcherBuilder.pattern("/js/**"),
                                mvcMatcherBuilder.pattern("/images/**")
                        ).permitAll()
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/editPizza"),
                                mvcMatcherBuilder.pattern("/addPizza")
                        ).hasRole("USER")
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/deletePizza")
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/Zaloguj")
                        .defaultSuccessUrl("/pizzas", true)
                        .permitAll());

        http.logout((logout) -> logout.permitAll());

        http.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable());
        http.headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()));

        http.exceptionHandling((config)-> config.accessDeniedPage("/url_error403")); //nie jest to nazwa widoku tylko url!!!

        return http.build();
    }


}
