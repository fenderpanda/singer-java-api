package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.VerificationCode;
import com.freedomfm.singer.exception.FailedCodeVerificationException;
import com.freedomfm.singer.repository.VerificationCodeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Value("${app.max-code-length}")
    private int MAX_CODE_LENGTH;
    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeServiceImpl(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public VerificationCode create(Singer singer) {
        VerificationCode code = verificationCodeRepository.findBySingerId(singer.getId())
                        .orElse(new VerificationCode());
        code.setSinger(singer);
        code.setCode(RandomStringUtils.randomAlphanumeric(MAX_CODE_LENGTH));

        return verificationCodeRepository.save(code);
    }

    @Override
    public boolean verify(String code, long singerId) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByCodeAndSingerId(code, singerId);

        if (verificationCode.isEmpty() || !verificationCode.get().getCode().equals(code)) {
            throw new FailedCodeVerificationException(singerId);
        }

        return true;
    }

    @Override
    public void delete(String code, long singerId) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByCodeAndSingerId(code, singerId);

        verificationCode.ifPresent(value -> verificationCodeRepository.deleteById(value.getId()));
    }
}
