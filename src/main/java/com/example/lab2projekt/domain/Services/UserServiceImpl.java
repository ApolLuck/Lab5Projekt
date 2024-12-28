package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Profile(ProfileNames.USERS_IN_DATABASE)
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    //@PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));

        var grantedAuthorities = user.getRoles().stream().
                map(x-> new SimpleGrantedAuthority(x.getType().toString())).
                collect(Collectors.toSet());

        return new User(
                user.getUsername(),                 // Nazwa użytkownika
                user.getPassword(),                 // Hasło
                user.isEnabled(),                   // Czy użytkownik jest aktywny (flaga enabled)
                true,                               // Czy konto nie wygasło
                true,                               // Czy dane logowania nie wygasły
                true,                               // Czy konto nie jest zablokowane
                grantedAuthorities                  // Role użytkownika (GrantedAuthority)
        );
    }
}
