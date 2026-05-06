package com.fst.elearning.repository;

import com.fst.elearning.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findTopByUtilisateur_EmailAndVerificationCodeAndUsedAtIsNullOrderByIdDesc(String email, String verificationCode);
}

