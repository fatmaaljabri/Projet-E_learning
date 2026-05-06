package com.fst.elearning.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    @Value("${spring.mail.username:}")
    private String smtpUsername;
    @Value("${spring.mail.password:}")
    private String smtpPassword;

    public MailService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSenderProvider = mailSenderProvider;
    }

    public void send(String to, String subject, String text) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            throw new RuntimeException("Service email non disponible (JavaMailSender manquant)");
        }
        if (smtpUsername == null || smtpUsername.isBlank() || smtpPassword == null || smtpPassword.isBlank()) {
            throw new RuntimeException("SMTP non configuré: remplissez spring.mail.username et spring.mail.password");
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Échec envoi email: " + e.getMessage(), e);
        }
    }
}

