package com.fst.elearning.repository;

import com.fst.elearning.entity.ProgressionLecon;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.entity.Lecon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressionLeconRepository extends JpaRepository<ProgressionLecon, Long> {

    Optional<ProgressionLecon> findByApprenantAndLecon(Utilisateur apprenant, Lecon lecon);

    List<ProgressionLecon> findByApprenantAndCompleteeTrue(Utilisateur apprenant);

    // ✅ Calcul du pourcentage de progression
    @Query("""
        SELECT
            CASE 
                WHEN COUNT(l) = 0 THEN 0
                ELSE (COUNT(pl) * 100.0 / COUNT(l))
            END
        FROM Cours c
        JOIN c.modules m
        JOIN m.lecons l
        LEFT JOIN l.progressions pl
            ON pl.apprenant = :apprenant AND pl.completee = true
        WHERE c.id = :coursId
    """)
    double calculerPourcentageProgression(
            @Param("apprenant") Utilisateur apprenant,
            @Param("coursId") Long coursId
    );

    // ✅ IDs des leçons complétées
    @Query("""
        SELECT pl.lecon.id 
        FROM ProgressionLecon pl
        WHERE pl.apprenant = :apprenant
        AND pl.lecon.module.cours.id = :coursId
        AND pl.completee = true
    """)
    List<Long> findLeconsCompleteesIds(
            @Param("apprenant") Utilisateur apprenant,
            @Param("coursId") Long coursId
    );
}