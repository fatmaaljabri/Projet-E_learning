package com.fst.elearning.service;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.UtilisateurRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utilisateur getUtilisateurConnecte() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public void validerDonneesInscription(Utilisateur utilisateur) {
        if (utilisateur.getEmail() == null || utilisateur.getEmail().isBlank()) {
            throw new RuntimeException("Email obligatoire");
        }
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        String mdp = utilisateur.getMotDePasse() != null ? utilisateur.getMotDePasse() : "";
        String email = utilisateur.getEmail() != null ? utilisateur.getEmail() : "";
        String nom = utilisateur.getNom() != null ? utilisateur.getNom() : "";
        String prenom = utilisateur.getPrenom() != null ? utilisateur.getPrenom() : "";

        if (mdp.length() < 8) {
            throw new RuntimeException("Mot de passe trop court (minimum 8 caractères)");
        }
        boolean hasLetter = mdp.chars().anyMatch(Character::isLetter);
        boolean hasDigit = mdp.chars().anyMatch(Character::isDigit);
        if (!hasLetter || !hasDigit) {
            throw new RuntimeException("Mot de passe invalide (doit contenir au moins une lettre et un chiffre)");
        }
        String mdpLower = mdp.toLowerCase();
        if (mdpLower.equals(email.toLowerCase())) {
            throw new RuntimeException("Le mot de passe doit être différent de l'email");
        }
        if (!nom.isBlank() && mdpLower.equals(nom.toLowerCase())) {
            throw new RuntimeException("Le mot de passe doit être différent du nom");
        }
        if (!prenom.isBlank() && mdpLower.equals(prenom.toLowerCase())) {
            throw new RuntimeException("Le mot de passe doit être différent du prénom");
        }
        String nomComplet = (prenom + " " + nom).trim().toLowerCase();
        if (!nomComplet.isBlank() && mdpLower.equals(nomComplet)) {
            throw new RuntimeException("Le mot de passe doit être différent du nom d'utilisateur");
        }
    }

    public Utilisateur inscrire(Utilisateur utilisateur) {
        validerDonneesInscription(utilisateur);
        // Ancien flux: compte inactif. Conservé pour compatibilité.
        utilisateur.setActif(false);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
