package com.freedomfm.singer.controller;

import com.freedomfm.singer.constraint.FileExtension;
import com.freedomfm.singer.constraint.FileSize;
import com.freedomfm.singer.exception.VerificationException;
import com.freedomfm.singer.service.SingerService;
import com.freedomfm.singer.service.SongService;
import com.freedomfm.singer.service.VerificationCodeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin({"http://localhost:3000", "https://smilingcatservice.com"})
@RequiredArgsConstructor
@Validated
public class SongController {
    private final VerificationCodeService codeService;
    private final SongService songService;
    private final SingerService singerService;

    @PostMapping("/songs")
    public long uploadSong(
            @RequestParam
            String code,
            @RequestParam
            long singerId,
            @RequestParam @NotNull @FileSize(30)
            @FileExtension(extensions = {"mp3", "wma", "aac", "wav", "m4a", "alac"})
            MultipartFile song) {
        if (!codeService.verify(code, singerId)) {
            throw new VerificationException(singerId, code);
        }

        codeService.delete(code, singerId);

        return songService.upload(song, singerService.getSinger(singerId));
    }
}
