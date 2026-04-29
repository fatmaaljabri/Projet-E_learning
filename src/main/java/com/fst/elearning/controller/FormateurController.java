package com.fst.elearning.controller;

import com.fst.elearning.entity.Cours;
import com.fst.elearning.entity.Lecon;
import com.fst.elearning.entity.Module; // ✅ BON IMPORT
import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/formateur")
public class FormateurController {

    private final UtilisateurService utilisateurService;
    private final CoursService coursService;
    private final ModuleService moduleService;
    private final LeconService leconService;
    private final InscriptionService inscriptionService;
    private final ProgressionService progressionService;

    public FormateurController(UtilisateurService utilisateurService,
                               CoursService coursService,
                               ModuleService moduleService,
                               LeconService leconService,
                               InscriptionService inscriptionService,
                               ProgressionService progressionService) {
        this.utilisateurService = utilisateurService;
        this.coursService = coursService;
        this.moduleService = moduleService;
        this.leconService = leconService;
        this.inscriptionService = inscriptionService;
        this.progressionService = progressionService;
    }

    // ===== Dashboard =====
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Utilisateur formateur = utilisateurService.getUtilisateurConnecte();
        var cours = coursService.getCoursByFormateur(formateur, 0, 10);

        model.addAttribute("formateur", formateur);
        model.addAttribute("cours", cours);
        model.addAttribute("totalCours", cours.getTotalElements());

        return "formateur/dashboard";
    }

    // ===== Liste des cours =====
    @GetMapping("/cours")
    public String mesCours(@RequestParam(defaultValue = "0") int page, Model model) {
        Utilisateur formateur = utilisateurService.getUtilisateurConnecte();
        var cours = coursService.getCoursByFormateur(formateur, page, 10);

        model.addAttribute("cours", cours);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cours.getTotalPages());

        return "formateur/cours-liste";
    }

    // ===== Créer un cours =====
    @GetMapping("/cours/nouveau")
    public String nouveauCoursForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("niveaux", Cours.Niveau.values());
        return "formateur/cours-form";
    }

    @PostMapping("/cours/nouveau")
    public String creerCours(@ModelAttribute Cours cours,
                             @RequestParam(required = false) MultipartFile image) throws IOException {

        Utilisateur formateur = utilisateurService.getUtilisateurConnecte();
        Cours created = coursService.creer(cours, image, formateur);

        return "redirect:/formateur/cours/" + created.getId() + "/modules";
    }

    // ===== Modifier un cours =====
    @GetMapping("/cours/{id}/modifier")
    public String modifierCoursForm(@PathVariable Long id, Model model) {
        model.addAttribute("cours", coursService.findById(id));
        model.addAttribute("niveaux", Cours.Niveau.values());
        return "formateur/cours-form";
    }

    @PostMapping("/cours/{id}/modifier")
    public String modifierCours(@PathVariable Long id,
                                @ModelAttribute Cours cours,
                                @RequestParam(required = false) MultipartFile image) throws IOException {

        coursService.modifier(id, cours, image);
        return "redirect:/formateur/cours";
    }

    // ===== Supprimer un cours =====
    @PostMapping("/cours/{id}/supprimer")
    public String supprimerCours(@PathVariable Long id) {
        coursService.supprimer(id);
        return "redirect:/formateur/cours";
    }

    // ===== Gestion des modules =====
    @GetMapping("/cours/{coursId}/modules")
    public String voirModules(@PathVariable Long coursId, Model model) {

        Cours cours = coursService.findById(coursId);

        model.addAttribute("cours", cours);
        model.addAttribute("modules", moduleService.findByCours(coursId));
        model.addAttribute("nouveauModule", new Module()); // ✅ OK

        return "formateur/modules";
    }

    @PostMapping("/cours/{coursId}/modules/nouveau")
    public String creerModule(@PathVariable Long coursId,
                              @ModelAttribute Module module) {

        moduleService.creer(module, coursId);
        return "redirect:/formateur/cours/" + coursId + "/modules";
    }

    @PostMapping("/modules/{moduleId}/supprimer")
    public String supprimerModule(@PathVariable Long moduleId,
                                  @RequestParam Long coursId) {

        moduleService.supprimer(moduleId);
        return "redirect:/formateur/cours/" + coursId + "/modules";
    }

    // ===== Gestion des leçons =====
    @GetMapping("/modules/{moduleId}/lecons")
    public String voirLecons(@PathVariable Long moduleId, Model model) {

        Module module = moduleService.findById(moduleId);

        model.addAttribute("module", module);
        model.addAttribute("lecons", leconService.findByModule(moduleId));
        model.addAttribute("nouvelleLecon", new Lecon());

        return "formateur/lecons";
    }

    @PostMapping("/modules/{moduleId}/lecons/nouveau")
    public String creerLecon(@PathVariable Long moduleId,
                             @ModelAttribute Lecon lecon) {

        leconService.creer(lecon, moduleId);
        return "redirect:/formateur/modules/" + moduleId + "/lecons";
    }

    @PostMapping("/lecons/{leconId}/supprimer")
    public String supprimerLecon(@PathVariable Long leconId,
                                 @RequestParam Long moduleId) {

        leconService.supprimer(leconId);
        return "redirect:/formateur/modules/" + moduleId + "/lecons";
    }

    // ===== Inscrits & Progression =====
    @GetMapping("/cours/{coursId}/inscrits")
    public String voirInscrits(@PathVariable Long coursId, Model model) {

        Cours cours = coursService.findById(coursId);
        var inscrits = inscriptionService.getInscritsParCours(cours);

        model.addAttribute("cours", cours);
        model.addAttribute("inscrits", inscrits);

        var progressions = inscrits.stream().map(insc -> {
            int pct = progressionService.getPourcentage(insc.getApprenant(), coursId);
            return new Object[]{ insc.getApprenant(), insc.getStatut(), pct };
        }).toList();

        model.addAttribute("progressions", progressions);

        return "formateur/inscrits";
    }
}