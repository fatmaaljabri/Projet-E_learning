package com.fst.elearning.repository;

import com.fst.elearning.entity.Lecon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeconRepository extends JpaRepository<Lecon, Long> {
    List<Lecon> findByModule_IdOrderByOrdreAsc(Long moduleId);
}
