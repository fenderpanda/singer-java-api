package com.freedomfm.singer.controller;

import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.service.SingerService;
import com.freedomfm.singer.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin({"http://localhost:3000", "https://smilingcatservice.com"})
@RequiredArgsConstructor
public class ModeratorController {
    private final SongService songService;
    private final SingerService singerService;

    @GetMapping("/singers")
    public List<Singer> getAllSingers() {
        return singerService.getParticipatedSingers();
    }

    @GetMapping("/songs/{song-id}")
    public double getSongRating(@PathVariable(value = "song-id") long songId) {
        return songService.getSongRating(songId);
    }

    @PutMapping("/songs/{song-id}")
    public long updateRating(@PathVariable(value = "song-id") long songId, @RequestParam double rating) {
        songService.updateRating(songId, rating);

        return songId;
    }
}
