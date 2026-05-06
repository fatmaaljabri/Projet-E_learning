package com.fst.elearning.service;

import com.fst.elearning.entity.ParentEnfant;
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.ParentEnfantRepository;
import com.fst.elearning.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentService {

    private final ParentEnfantRepository parentEnfantRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ParentService(ParentEnfantRepository parentEnfantRepository,
                         UtilisateurRepository utilisateurRepository) {
        this.parentEnfantRepository = parentEnfantRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<ParentEnfant> getEnfants(Utilisateur parent) {
        return parentEnfantRepository.findByParent(parent);
    }

    @Transactional
    public void lierEnfant(Utilisateur parent, String enfantEmail) {
        if (enfantEmail == null || enfantEmail.isBlank()) {
            throw new RuntimeException("Email enfant obligatoire");
        }
        Utilisateur enfant = utilisateurRepository.findByEmail(enfantEmail.trim())
                .orElseThrow(() -> new RuntimeException("Enfant introuvable (email)"));
        if (enfant.getRole() != Utilisateur.Role.APPRENANT) {
            throw new RuntimeException("Le compte enfant doit être APPRENANT");
        }
        parentEnfantRepository.findByParentAndEnfant(parent, enfant)
                .ifPresent(pe -> { throw new RuntimeException("Déjà lié"); });

        parentEnfantRepository.save(ParentEnfant.builder()
                .parent(parent)
                .enfant(enfant)
                .build());
    }
}

