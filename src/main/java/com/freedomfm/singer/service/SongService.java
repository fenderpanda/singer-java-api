package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {
    long upload(MultipartFile song, Singer singer);
    double getSongRating(long songId);
    void updateRating(long songId, double rating);
}
