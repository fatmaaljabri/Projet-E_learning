package com.fst.elearning.repository;

import com.fst.elearning.entity.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {
    Optional<PendingRegistration> findByEmail(String email);
    Optional<PendingRegistration> findByToken(String token);
    Optional<PendingRegistration> findByEmailAndVerificationCode(String email, String verificationCode);
}

