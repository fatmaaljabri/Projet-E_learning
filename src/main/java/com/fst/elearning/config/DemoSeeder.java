package com.fst.elearning.config;

import com.fst.elearning.entity.Cours;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.CoursRepository;
import com.fst.elearning.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DemoSeeder {

    @Bean
    CommandLineRunner seedDemo(UtilisateurRepository utilisateurRepository,
                               CoursRepository coursRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Ajoute des infos formateurs si manquantes + quelques cours d'exemple
            List<Utilisateur> formateurs = utilisateurRepository.findByRole(Utilisateur.Role.FORMATEUR);
            for (Utilisateur f : formateurs) {
                boolean changed = false;
                if (f.getSpecialite() == null) {
                    f.setSpecialite("Développement & Pédagogie");
                    changed = true;
                }
                if (f.getBio() == null) {
                    f.setBio("Formateur Dourousi Academy. Approche pratique orientée projets, quiz et suivi.");
                    changed = true;
                }
                if (f.getPhotoUrl() == null) {
                    f.setPhotoUrl("https://placehold.co/128x128/111827/ffffff?text=DA");
                    changed = true;
                }
                if (changed) utilisateurRepository.save(f);
            }

            // Crée un compte parent démo si absent
            utilisateurRepository.findByEmail("parent@dourousi.tn").orElseGet(() -> {
                Utilisateur parent = Utilisateur.builder()
                        .email("parent@dourousi.tn")
                        .motDePasse(passwordEncoder.encode("Parent1234"))
                        .nom("Parent")
                        .prenom("Demo")
                        .role(Utilisateur.Role.PARENT)
                        .specialite("Espace parent")
                        .bio("Compte de démonstration pour le suivi des enfants.")
                        .photoUrl("https://placehold.co/128x128/111827/ffffff?text=P")
                        .build();
                return utilisateurRepository.save(parent);
            });

            // Si pas de cours “premium” (prix>0), on en ajoute quelques uns inspirés.
            if (coursRepository.count() < 6 && !formateurs.isEmpty()) {
                Utilisateur f = formateurs.get(0);
                createIfNotExists(coursRepository, f, "Google Data Analytics — Fondamentaux", "Data", Cours.Niveau.DEBUTANT, new BigDecimal("59.00"));
                createIfNotExists(coursRepository, f, "Google UX Design — De 0 à Prototype", "Design", Cours.Niveau.DEBUTANT, new BigDecimal("69.00"));
                createIfNotExists(coursRepository, f, "IT Support — Réseau & Dépannage", "IT", Cours.Niveau.INTERMEDIAIRE, new BigDecimal("79.00"));
            }
        };
    }

    private void createIfNotExists(CoursRepository coursRepository,
                                   Utilisateur formateur,
                                   String titre,
                                   String categorie,
                                   Cours.Niveau niveau,
                                   BigDecimal prix) {
        boolean exists = coursRepository.findAll().stream().anyMatch(c -> titre.equalsIgnoreCase(c.getTitre()));
        if (exists) return;
        Cours c = Cours.builder()
                .titre(titre)
                .categorie(categorie)
                .niveau(niveau)
                .description("Cours d'exemple (inspiré). Ajoutez modules, leçons, PDF et quiz pour compléter le parcours.")
                .formateur(formateur)
                .prix(prix)
                .actif(true)
                .build();
        coursRepository.save(c);
    }
}

