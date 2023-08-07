package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.exception.SingerNotFoundException;
import com.freedomfm.singer.repository.SingerRepository;
import com.freedomfm.singer.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SingerServiceImpl implements SingerService {
    @Value("${app.max-songs}")
    private int MAX_SONGS;
    private final SingerRepository singerRepository;
    private final SongRepository songRepository;

    @Override
    public Singer getSinger(String email) {
        return singerRepository.findByEmail(email);
    }

    @Override
    public Singer getSinger(long singerId) {
        return singerRepository.findById(singerId)
                .orElseThrow(() -> new SingerNotFoundException(singerId));
    }

    @Override
    public List<Singer> getAllSingers() {
        return singerRepository.findAll();
    }

    @Override
    public List<Singer> getParticipatedSingers() {
        List<Singer> singers = singerRepository.findAll();

        return singers.stream().filter(singer -> singer.getSongs().size() > 0).collect(Collectors.toList());
    }


    @Override
    public Singer create(String email, String name, String phone) {
        Singer singer = new Singer();
        singer.setEmail(email);
        singer.setName(name);
        singer.setPhone(phone);

        return singerRepository.save(singer);
    }

    @Override
    public boolean isRegistered(String email) {
        return singerRepository.existsByEmail(email);
    }

    @Override
    public boolean isParticipated(long singerId) {
        int songAmount = songRepository.countAllBySingerId(singerId);

        return songAmount >= MAX_SONGS;
    }
}
