package com.fst.elearning.service;

import com.fst.elearning.entity.Cours;
import com.fst.elearning.entity.Module;
import com.fst.elearning.repository.CoursRepository;
import com.fst.elearning.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CoursRepository coursRepository;

    public ModuleService(ModuleRepository moduleRepository, CoursRepository coursRepository) {
        this.moduleRepository = moduleRepository;
        this.coursRepository = coursRepository;
    }

    public Module findById(Long id) {
        return moduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Module non trouvé"));
    }

    public List<Module> findByCours(Long coursId) {
        return moduleRepository.findByCours_IdOrderByOrdreAsc(coursId);
    }

    public Module creer(Module module, Long coursId) {
        Cours cours = coursRepository.findById(coursId)
            .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        ((Module) module).setCours(cours);
        return moduleRepository.save(module);
    }

    public Module modifier(Long id, Module moduleModif) {
        Module module = findById(id);
        module.setTitre(moduleModif.getTitre());
        module.setDescription(moduleModif.getDescription());
        module.setOrdre(moduleModif.getOrdre());
        return moduleRepository.save(module);
    }

    public void supprimer(Long id) {
        moduleRepository.deleteById(id);
    }
}
