package com.fst.elearning.controller;

import com.fst.elearning.dto.ProgressionDTO;
import com.fst.elearning.entity.*;
import com.fst.elearning.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/apprenant")
public class ApprenantController {

    private final UtilisateurService utilisateurService;
    private final InscriptionService inscriptionService;
    private final ProgressionService progressionService;
    private final CoursService coursService;
    private final QuizService quizService;
    private final LeconService leconService;

    public ApprenantController(UtilisateurService utilisateurService,
                                InscriptionService inscriptionService,
                                ProgressionService progressionService,
                                CoursService coursService,
                                QuizService quizService,
                                LeconService leconService) {
        this.utilisateurService = utilisateurService;
        this.inscriptionService = inscriptionService;
        this.progressionService = progressionService;
        this.coursService = coursService;
        this.quizService = quizService;
        this.leconService = leconService;
    }

    // ===== Dashboard apprenant =====
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Utilisateur apprenant = utilisateurService.getUtilisateurConnecte();
        List<ProgressionDTO> progressions = progressionService.getProgressionsPourApprenant(apprenant);

        // ✅ CORRECTION : calcul fait en Java, pas dans Thymeleaf (les lambdas ne sont pas supportées)
        long coursTermines = progressions.stream()
                .filter(p -> "TERMINE".equals(p.getStatut()))
                .count();

        model.addAttribute("apprenant", apprenant);
        model.addAttribute("progressions", progressions);
        model.addAttribute("coursTermines", coursTermines); // ✅ variable ajoutée
        model.addAttribute("resultats", quizService.getResultatsApprenant(apprenant));
        return "apprenant/dashboard";
    }

    // ===== Inscription à un cours =====
    @PostMapping("/inscrire/{coursId}")
    public String inscrire(@PathVariable Long coursId) {
        Utilisateur apprenant = utilisateurService.getUtilisateurConnecte();
        inscriptionService.inscrire(apprenant, coursId);
        return "redirect:/apprenant/cours/" + coursId;
    }

    // ===== Accès à un cours (contenu) =====
    @GetMapping("/cours/{coursId}")
    public String voirCours(@PathVariable Long coursId, Model model) {
        Utilisateur apprenant = utilisateurService.getUtilisateurConnecte();
        Cours cours = coursService.findById(coursId);

        if (!inscriptionService.estInscrit(apprenant, cours)) {
            return "redirect:/cours/" + coursId + "/detail";
        }

        List<Long> completes = progressionService.getLeconsCompleteesIds(apprenant, coursId);
        int pct = progressionService.getPourcentage(apprenant, coursId);

        model.addAttribute("cours", cours);
        model.addAttribute("leconsCompleteesIds", completes);
        model.addAttribute("pourcentage", pct);
        return "apprenant/cours-contenu";
    }

    // ===== Marquer leçon complétée =====
    @PostMapping("/lecon/{leconId}/complete")
    public String marquerComplete(@PathVariable Long leconId,
                                   @RequestParam Long coursId) {
        Utilisateur apprenant = utilisateurService.getUtilisateurConnecte();
        progressionService.marquerLeconComplete(apprenant, leconId);
        return "redirect:/apprenant/cours/" + coursId;
    }

    // ===== Quiz =====
    @GetMapping("/quiz/{quizId}")
    public String voirQuiz(@PathVariable Long quizId, Model model) {
        Quiz quiz = quizService.findById(quizId);
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quizService.getQuestions(quizId));
        return "apprenant/quiz";
    }

    @PostMapping("/quiz/{quizId}/soumettre")
    public String soumettreQuiz(@PathVariable Long quizId,
                                 @RequestParam Map<String, String> params,
                                 Model model) {
        Utilisateur apprenant = utilisateurService.getUtilisateurConnecte();

        Map<Long, String> reponses = new HashMap<>();
        params.forEach((key, val) -> {
            if (key.startsWith("question_")) {
                try {
                    Long qId = Long.parseLong(key.replace("question_", ""));
                    reponses.put(qId, val);
                } catch (NumberFormatException ignored) {}
            }
        });

        var result = quizService.soumettre(apprenant, quizId, reponses);
        model.addAttribute("result", result);
        return "apprenant/quiz-result";
    }
}