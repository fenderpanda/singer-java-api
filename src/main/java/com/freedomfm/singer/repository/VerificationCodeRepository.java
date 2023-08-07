package com.freedomfm.singer.repository;

import com.freedomfm.singer.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByCodeAndSingerId(String code, long singerId);
    Optional<VerificationCode> findBySingerId(long singerId);
}
