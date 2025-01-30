package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.Order;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
        helper.setFrom("noreply@uph.edu.pl");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // wysyłanie w formacie HTML

        emailSender.send(mimeMessage);
    }

    public void sendConfirmedEmail(Map<String, String> params, Order order) throws MessagingException {
        String clientEmail = params.get("email");
        String subject = "Złożenie zamówienia nr: " + order.getId();
        String text = "Dziękujemy za złożenie zamówienia!\n+" +
                "Sczegóły zamówienia możesz sprawdzić pod adresem: \n" +
                "Aby sprawdzić zamówienie będzie potrzebny Twój email oraz nr zamówienia.\n" +
                "Dziękujemy i życzymy smacznego! Do zobaczenia!";

        var mimeMessage = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom("pizzeria@uph.edu.pl");
        helper.setTo(clientEmail);
        helper.setSubject(subject);
        helper.setText(text, true); //

        emailSender.send(mimeMessage);
    }
}
