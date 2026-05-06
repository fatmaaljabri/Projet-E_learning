package com.fst.elearning.controller;

import com.fst.elearning.entity.Utilisateur;
import com.fst.elearning.repository.ReponseApprenantRepository;
import com.fst.elearning.service.ParentService;
import com.fst.elearning.service.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/parent")
public class ParentController {

    private final UtilisateurService utilisateurService;
    private final ParentService parentService;
    private final ReponseApprenantRepository reponseApprenantRepository;

    public ParentController(UtilisateurService utilisateurService,
                            ParentService parentService,
                            ReponseApprenantRepository reponseApprenantRepository) {
        this.utilisateurService = utilisateurService;
        this.parentService = parentService;
        this.reponseApprenantRepository = reponseApprenantRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String erreur,
                            @RequestParam(required = false) String success,
                            Model model) {
        Utilisateur parent = utilisateurService.getUtilisateurConnecte();
        var liens = parentService.getEnfants(parent);

        model.addAttribute("parent", parent);
        model.addAttribute("liens", liens);
        model.addAttribute("erreur", erreur);
        model.addAttribute("success", success);

        // résultats par enfant
        var resultatsParEnfant = liens.stream().map(lien -> new Object[]{
                lien.getEnfant(),
                reponseApprenantRepository.findByApprenant(lien.getEnfant())
        }).toList();
        model.addAttribute("resultatsParEnfant", resultatsParEnfant);

        return "parent/dashboard";
    }

    @PostMapping("/lier-enfant")
    public String lier(@RequestParam String enfantEmail) {
        try {
            Utilisateur parent = utilisateurService.getUtilisateurConnecte();
            parentService.lierEnfant(parent, enfantEmail);
            return "redirect:/parent/dashboard?success=Enfant%20li%C3%A9";
        } catch (RuntimeException e) {
            return "redirect:/parent/dashboard?erreur=" + e.getMessage().replace(" ", "%20");
        }
    }
}

