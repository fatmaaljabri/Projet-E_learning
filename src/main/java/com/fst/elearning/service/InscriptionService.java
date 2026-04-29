package com.fst.elearning.service;

import com.fst.elearning.entity.*;
import com.fst.elearning.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final CoursRepository coursRepository;

    public InscriptionService(InscriptionRepository inscriptionRepository,
                               CoursRepository coursRepository) {
        this.inscriptionRepository = inscriptionRepository;
        this.coursRepository = coursRepository;
    }

    @Transactional
    public Inscription inscrire(Utilisateur apprenant, Long coursId) {
        Cours cours = coursRepository.findById(coursId)
            .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        if (inscriptionRepository.existsByApprenantAndCours(apprenant, cours)) {
            throw new RuntimeException("Déjà inscrit à ce cours");
        }

        Inscription inscription = Inscription.builder()
            .apprenant(apprenant)
            .cours(cours)
            .statut(Inscription.Statut.EN_COURS)
            .build();

        return inscriptionRepository.save(inscription);
    }

    public boolean estInscrit(Utilisateur apprenant, Cours cours) {
        return inscriptionRepository.existsByApprenantAndCours(apprenant, cours);
    }

    public List<Inscription> getMesInscriptions(Utilisateur apprenant) {
        return inscriptionRepository.findByApprenant(apprenant);
    }

    public List<Inscription> getInscritsParCours(Cours cours) {
        return inscriptionRepository.findByCours(cours);
    }
}
