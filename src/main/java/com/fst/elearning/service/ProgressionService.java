package com.fst.elearning.service;

import com.fst.elearning.dto.ProgressionDTO;
import com.fst.elearning.entity.*;
import com.fst.elearning.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressionService {

    private final ProgressionLeconRepository progressionRepository;
    private final LeconRepository leconRepository;
    private final InscriptionRepository inscriptionRepository;
    private final CoursRepository coursRepository;

    public ProgressionService(ProgressionLeconRepository progressionRepository,
                               LeconRepository leconRepository,
                               InscriptionRepository inscriptionRepository,
                               CoursRepository coursRepository) {
        this.progressionRepository = progressionRepository;
        this.leconRepository = leconRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.coursRepository = coursRepository;
    }

    @Transactional
    public void marquerLeconComplete(Utilisateur apprenant, Long leconId) {
        Lecon lecon = leconRepository.findById(leconId)
            .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));

        ProgressionLecon progression = progressionRepository
            .findByApprenantAndLecon(apprenant, lecon)
            .orElse(ProgressionLecon.builder().apprenant(apprenant).lecon(lecon).build());

        progression.setCompletee(true);
        progression.setDateCompletion(LocalDateTime.now());
        progressionRepository.save(progression);

        // Vérifier si le cours est complété à 100%
        Long coursId = lecon.getModule().getCours().getId();
        int pourcentage = (int) progressionRepository.calculerPourcentageProgression(apprenant, coursId);
        if (pourcentage >= 100) {
            Cours cours = coursRepository.findById(coursId).orElseThrow();
            inscriptionRepository.findByApprenantAndCours(apprenant, cours).ifPresent(insc -> {
                insc.setStatut(Inscription.Statut.TERMINE);
                inscriptionRepository.save(insc);
            });
        }
    }

    public int getPourcentage(Utilisateur apprenant, Long coursId) {
        return (int) progressionRepository.calculerPourcentageProgression(apprenant, coursId);
    }

    public List<Long> getLeconsCompleteesIds(Utilisateur apprenant, Long coursId) {
        return progressionRepository.findLeconsCompleteesIds(apprenant, coursId);
    }

    public List<ProgressionDTO> getProgressionsPourApprenant(Utilisateur apprenant) {
        return inscriptionRepository.findByApprenant(apprenant).stream().map(insc -> {
            Cours cours = insc.getCours();
            int pct = (int) progressionRepository.calculerPourcentageProgression(apprenant, cours.getId());
            int total = cours.getNombreLecons();
            int completees = (int) Math.round(pct / 100.0 * total);
            return ProgressionDTO.builder()
                .coursId(cours.getId())
                .coursTitre(cours.getTitre())
                .pourcentage(pct)
                .leconsCompletees(completees)
                .totalLecons(total)
                .statut(insc.getStatut().name())
                .build();
        }).collect(Collectors.toList());
    }
}
