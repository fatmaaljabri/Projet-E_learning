package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 120)
    private String specialite;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String photoUrl;

    private boolean actif = true;

    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL)
    private List<Cours> coursCreés;

    @OneToMany(mappedBy = "apprenant", cascade = CascadeType.ALL)
    private List<Inscription> inscriptions;

    public enum Role {
        ADMIN, FORMATEUR, APPRENANT, PARENT
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
