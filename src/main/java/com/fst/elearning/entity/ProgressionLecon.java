package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progression_lecons",
       uniqueConstraints = @UniqueConstraint(columnNames = {"apprenant_id", "lecon_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgressionLecon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apprenant_id", nullable = false)
    private Utilisateur apprenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecon_id", nullable = false)
    private Lecon lecon;

    @Column(nullable = false)
    private boolean completee = false;

    private LocalDateTime dateCompletion;
}
