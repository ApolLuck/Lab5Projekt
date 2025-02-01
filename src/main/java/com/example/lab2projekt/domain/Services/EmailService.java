package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.Order;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendActivationEmail(String to, String activationCode) throws MessagingException {
        String subject = "Aktywacja konta";
        String text = "Kliknij tutaj, aby aktywować swoje konto: " +
                "http://localhost:8080/activate?code=" + activationCode;

        var mimeMessage = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom("pizzeria@uph.edu.pl");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // wysyłanie w formacie HTML

        emailSender.send(mimeMessage);
    }

    public void sendConfirmedEmail(String clientEmail, Order order) throws MessagingException {
        String subject = "Potwierdzenie zamówienia nr: " + order.getId();

        // HTML format
        String text = "<html>" +
                "<body>" +
                "<h2>Dziękujemy za złożenie zamówienia!</h2>" +
                "<p>Twoje zamówienie zostało pomyślnie przyjęte. Szczegóły zamówienia znajdziesz poniżej:</p>" +
                "<p><strong>Numer zamówienia:</strong> " + order.getId() + "</p>" +
                "<p><strong>Data złożenia zamówienia:</strong> " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "</p>" +
                "<p>Możesz sprawdzić szczegóły zamówienia, klikając poniższy link:</p>" +
                "<p><a href=\"http://localhost:8080/yourOrder" + "\">Sprawdź swoje zamówienie</a></p>" +
                "<p>Do sprawdzenia zamówienia będzie potrzebny Twój email oraz numer zamówienia.</p>" +
                "<p>Życzymy smacznego i dziękujemy za zaufanie!<br>Do zobaczenia w naszej pizzerii!</p>" +
                "</body>" +
                "</html>";

        var mimeMessage = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom("pizzeria@uph.edu.pl");
        helper.setTo(clientEmail);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(mimeMessage);
    }

}
