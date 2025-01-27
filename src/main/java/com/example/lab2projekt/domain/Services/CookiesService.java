package com.example.lab2projekt.domain.Services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CookiesService {

    public String getCookiesForUser(HttpServletRequest request, HttpServletResponse response){
        String userCookieId = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userSessionId".equals(cookie.getName())) {
                    userCookieId = cookie.getValue();
                    break;
                }
            }
        }

        // Jeśli ciasteczko nie istnieje, utwórz je
        if (userCookieId == null) {
            userCookieId = UUID.randomUUID().toString();
            Cookie newCookie = new Cookie("userSessionId", userCookieId);
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60); // Ciasteczko ważne przez 7 dni
            response.addCookie(newCookie);
        }
        return userCookieId;
    }

}
