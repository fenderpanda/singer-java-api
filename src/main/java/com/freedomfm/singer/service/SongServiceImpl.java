package com.freedomfm.singer.service;

import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.Song;
import com.freedomfm.singer.exception.SongNotFoundException;
import com.freedomfm.singer.exception.UploadFileException;
import com.freedomfm.singer.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    @Value("${app.path.uploadDir}")
    private String uploadDir;

    private final SongRepository songRepository;
    private final EmailService emailService;

    @Override
    public long upload(MultipartFile file, Singer singer) {
        String filename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename);
        String uuid = UUID.randomUUID().toString();
        String uuidName = uuid + "." + extension;

        try {
            Files.copy(file.getInputStream(), Paths.get(uploadDir).resolve(uuidName));
        } catch (IOException e) {
            throw new UploadFileException(singer.getId(), filename, e);
        }

        Song song = new Song();
        song.setOriginalName(filename);
        song.setUuidName(uuidName);
        song.setSinger(singer);
        songRepository.save(song);

        emailService.sendCongratulation(singer);

        return song.getId();
    }

    @Override
    public double getSongRating(long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(songId));

        return song.getRating();
    }

    @Override
    public void updateRating(long songId, double rating) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(songId));

        song.setRating(rating);

        songRepository.save(song);
    }
}
