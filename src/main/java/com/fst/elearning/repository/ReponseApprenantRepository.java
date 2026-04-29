package com.fst.elearning.repository;

import com.fst.elearning.entity.ReponseApprenant;
import com.fst.elearning.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReponseApprenantRepository extends JpaRepository<ReponseApprenant, Long> {
    List<ReponseApprenant> findByApprenant(Utilisateur apprenant);
    Optional<ReponseApprenant> findByApprenantAndQuiz_Id(Utilisateur apprenant, Long quizId);
    List<ReponseApprenant> findByQuiz_Id(Long quizId);
}
