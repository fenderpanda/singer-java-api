package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;

public interface EmailService {
    void sendVerificationCode(Singer singer, String verificationCode);
    void sendCongratulation(Singer singer);
}
