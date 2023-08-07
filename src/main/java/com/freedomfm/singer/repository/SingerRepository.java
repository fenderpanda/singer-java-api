package com.freedomfm.singer.repository;

import com.freedomfm.singer.entity.Singer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingerRepository extends JpaRepository<Singer, Long> {
    Singer findByEmail(String email);
    boolean existsByEmail(String email);
}
