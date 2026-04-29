package com.fst.elearning.repository;

import com.fst.elearning.entity.Cours;
import com.fst.elearning.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, Long> {

    // Catalogue public paginé avec filtres
    @Query("""
        SELECT c FROM Cours c
        WHERE c.actif = true
        AND (:titre IS NULL OR LOWER(c.titre) LIKE LOWER(CONCAT('%', :titre, '%')))
        AND (:categorie IS NULL OR c.categorie = :categorie)
        AND (:niveau IS NULL OR c.niveau = :niveau)
        ORDER BY c.dateCreation DESC
    """)
    Page<Cours> findCatalogueFiltre(
        @Param("titre") String titre,
        @Param("categorie") String categorie,
        @Param("niveau") Cours.Niveau niveau,
        Pageable pageable
    );

    // Cours d'un formateur
    Page<Cours> findByFormateurOrderByDateCreationDesc(Utilisateur formateur, Pageable pageable);

    // Toutes les catégories distinctes
    @Query("SELECT DISTINCT c.categorie FROM Cours c WHERE c.actif = true AND c.categorie IS NOT NULL")
    List<String> findAllCategories();

    // Statistiques admin
    long countByActifTrue();
}
