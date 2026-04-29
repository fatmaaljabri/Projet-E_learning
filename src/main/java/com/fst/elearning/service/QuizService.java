package com.fst.elearning.service;

import com.fst.elearning.dto.QuizResultDTO;
import com.fst.elearning.entity.*;
import com.fst.elearning.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ReponseApprenantRepository reponseRepository;

    public QuizService(QuizRepository quizRepository,
                       QuestionRepository questionRepository,
                       ReponseApprenantRepository reponseRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.reponseRepository = reponseRepository;
    }

    public Quiz findById(Long id) {
        return quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz non trouvé"));
    }

    public Quiz findByModuleId(Long moduleId) {
        return quizRepository.findByModule_Id(moduleId).orElse(null);
    }

    public List<Question> getQuestions(Long quizId) {
        return questionRepository.findByQuiz_Id(quizId);
    }

    @Transactional
    public QuizResultDTO soumettre(Utilisateur apprenant, Long quizId, Map<Long, String> reponses) {
        Quiz quiz = findById(quizId);
        List<Question> questions = getQuestions(quizId);

        int score = 0;
        for (Question q : questions) {
            String rep = reponses.get(q.getId());
            if (q.getBonneReponse().equalsIgnoreCase(rep)) score++;
        }

        int total = questions.size();
        double pourcentage = total > 0 ? (double) score / total * 100 : 0;

        ReponseApprenant result = ReponseApprenant.builder()
            .apprenant(apprenant)
            .quiz(quiz)
            .score(score)
            .totalQuestions(total)
            .build();
        reponseRepository.save(result);

        return QuizResultDTO.builder()
            .quizId(quizId)
            .quizTitre(quiz.getTitre())
            .score(score)
            .totalQuestions(total)
            .pourcentage(pourcentage)
            .reussi(pourcentage >= 60)
            .build();
    }

    public List<ReponseApprenant> getResultatsApprenant(Utilisateur apprenant) {
        return reponseRepository.findByApprenant(apprenant);
    }
}
