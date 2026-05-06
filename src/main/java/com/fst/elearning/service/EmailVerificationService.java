package com.fst.elearning.service;

import com.fst.elearning.entity.PendingRegistration;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.PendingRegistrationRepository;
import com.fst.elearning.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailVerificationService(PendingRegistrationRepository pendingRegistrationRepository,
                                    UtilisateurRepository utilisateurRepository,
                                    UtilisateurService utilisateurService,
                                    PasswordEncoder passwordEncoder,
                                    MailService mailService) {
        this.pendingRegistrationRepository = pendingRegistrationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    public String createAndSend(Utilisateur utilisateur) {
        utilisateurService.validerDonneesInscription(utilisateur);

        String token = UUID.randomUUID().toString().replace("-", "");
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        String email = utilisateur.getEmail().trim().toLowerCase();

        pendingRegistrationRepository.findByEmail(email).ifPresent(pendingRegistrationRepository::delete);

        PendingRegistration record = PendingRegistration.builder()
                .email(email)
                .motDePasseHash(passwordEncoder.encode(utilisateur.getMotDePasse()))
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .role(utilisateur.getRole())
                .token(token)
                .verificationCode(code)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        pendingRegistrationRepository.save(record);

        String link = baseUrl + "/auth/verify?token=" + token;
        String subject = "Vérifiez votre email — Dourousi Academy";
        String text = """
                Bienvenue sur Dourousi Academy !

                Code de vérification : %s

                Pour activer votre compte, cliquez sur ce lien (valable 24h) :
                %s

                Si vous n'êtes pas à l'origine de cette inscription, ignorez cet email.
                """.formatted(code, link);

        mailService.send(email, subject, text);

        return link;
    }

    @Transactional
    public void verify(String token) {
        PendingRegistration rec = pendingRegistrationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Lien de vérification invalide"));
        createAccountFromPending(rec);
    }

    @Transactional
    public void verifyByCode(String email, String code) {
        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            throw new RuntimeException("Email et code sont obligatoires");
        }
        PendingRegistration rec = pendingRegistrationRepository
                .findByEmailAndVerificationCode(email.trim().toLowerCase(), code.trim())
                .orElseThrow(() -> new RuntimeException("Code invalide"));
        createAccountFromPending(rec);
    }

    private void createAccountFromPending(PendingRegistration rec) {
        if (rec.isExpired()) throw new RuntimeException("Lien expiré");
        if (utilisateurRepository.existsByEmail(rec.getEmail())) {
            pendingRegistrationRepository.delete(rec);
            throw new RuntimeException("Compte déjà créé pour cet email");
        }

        Utilisateur user = Utilisateur.builder()
                .email(rec.getEmail())
                .motDePasse(rec.getMotDePasseHash())
                .nom(rec.getNom())
                .prenom(rec.getPrenom())
                .role(rec.getRole())
                .actif(true)
                .build();
        user.setActif(true);
        utilisateurRepository.save(user);
        pendingRegistrationRepository.delete(rec);
    }
}

