package com.fst.elearning.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents_enfants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"parent_id", "enfant_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentEnfant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Utilisateur parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfant_id", nullable = false)
    private Utilisateur enfant;
}

