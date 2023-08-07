package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.VerificationCode;

public interface VerificationCodeService {
    VerificationCode create(Singer singer);
    boolean verify(String code, long singerId);
    void delete(String code, long singerId);
}
