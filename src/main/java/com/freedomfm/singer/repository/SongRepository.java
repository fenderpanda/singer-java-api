package com.freedomfm.singer.repository;

import com.freedomfm.singer.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
    int countAllBySingerId(long singerId);
}
