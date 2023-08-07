package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface SingerService {
    Singer getSinger(String email);
    Singer getSinger(long singerId);
    List<Singer> getAllSingers();
    List<Singer> getParticipatedSingers();
    Singer create(String email, String name, String phone);
    boolean isRegistered(String email);
    boolean isParticipated(long singerId);
}
