package com.fst.elearning.repository;

import com.fst.elearning.entity.Inscription;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    Optional<Inscription> findByApprenantAndCours(Utilisateur apprenant, Cours cours);

    List<Inscription> findByApprenant(Utilisateur apprenant);

    List<Inscription> findByCours(Cours cours);

    boolean existsByApprenantAndCours(Utilisateur apprenant, Cours cours);

    long count();
}
