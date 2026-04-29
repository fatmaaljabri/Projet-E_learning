package com.fst.elearning.service;

import com.fst.elearning.entity.Lecon;
import com.fst.elearning.entity.Module;
import com.fst.elearning.repository.LeconRepository;
import com.fst.elearning.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeconService {

    private final LeconRepository leconRepository;
    private final ModuleRepository moduleRepository;

    public LeconService(LeconRepository leconRepository, ModuleRepository moduleRepository) {
        this.leconRepository = leconRepository;
        this.moduleRepository = moduleRepository;
    }

    public Lecon findById(Long id) {
        return leconRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));
    }

    public List<Lecon> findByModule(Long moduleId) {
        return leconRepository.findByModule_IdOrderByOrdreAsc(moduleId);
    }

    public Lecon creer(Lecon lecon, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
            .orElseThrow(() -> new RuntimeException("Module non trouvé"));
        lecon.setModule(module);
        return leconRepository.save(lecon);
    }

    public Lecon modifier(Long id, Lecon leconModif) {
        Lecon lecon = findById(id);
        lecon.setTitre(leconModif.getTitre());
        lecon.setContenu(leconModif.getContenu());
        lecon.setOrdre(leconModif.getOrdre());
        lecon.setDureeMin(leconModif.getDureeMin());
        return leconRepository.save(lecon);
    }

    public void supprimer(Long id) {
        leconRepository.deleteById(id);
    }
}
