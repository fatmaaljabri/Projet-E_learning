package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscriptions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"apprenant_id", "cours_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apprenant_id", nullable = false)
    private Utilisateur apprenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(nullable = false)
    private LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut;

    @PrePersist
    public void prePersist() {
        if (dateInscription == null) dateInscription = LocalDate.now();
        if (statut == null) statut = Statut.EN_COURS;
    }

    public enum Statut {
        EN_COURS, TERMINE, ABANDONNE
    }
}
