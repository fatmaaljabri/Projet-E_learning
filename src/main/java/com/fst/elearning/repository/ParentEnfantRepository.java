package com.fst.elearning.repository;

import com.fst.elearning.entity.ParentEnfant;
import com.fst.elearning.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParentEnfantRepository extends JpaRepository<ParentEnfant, Long> {
    List<ParentEnfant> findByParent(Utilisateur parent);
    Optional<ParentEnfant> findByParentAndEnfant(Utilisateur parent, Utilisateur enfant);
}

