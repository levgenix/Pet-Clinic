package com.vet24.util.mailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PetFoundMailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public PetFoundMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTextAndGeolocationPet(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
