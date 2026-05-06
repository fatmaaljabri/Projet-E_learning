package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cours")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String categorie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Niveau niveau;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal prix = BigDecimal.ZERO;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean actif = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formateur_id", nullable = false)
    private Utilisateur formateur;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre ASC")
    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Inscription> inscriptions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        dateCreation = LocalDateTime.now();
    }

    public enum Niveau {
        DEBUTANT, INTERMEDIAIRE, AVANCE
    }

    public int getNombreInscrits() {
        return inscriptions != null ? inscriptions.size() : 0;
    }

    public int getNombreLecons() {
        if (modules == null) return 0;
        return modules.stream()
                .mapToInt(m -> m.getLecons() != null ? m.getLecons().size() : 0)
                .sum();
    }
}
