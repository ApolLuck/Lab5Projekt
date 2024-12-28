package com.example.lab2projekt.domain.Services;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
}
