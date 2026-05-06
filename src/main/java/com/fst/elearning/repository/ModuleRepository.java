package com.fst.elearning.repository;

import com.fst.elearning.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    
    List<Module> findByCours_IdOrderByOrdreAsc(Long coursId);

    @Query("SELECT m FROM Module m JOIN FETCH m.cours")  // ← ajoute ça
    List<Module> findAllWithCours();
}