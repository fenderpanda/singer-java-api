package com.freedomfm.singer.controller;

import com.freedomfm.singer.SingerApplicationTests;
import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.Song;
import com.freedomfm.singer.repository.SongRepository;
import com.freedomfm.singer.service.SingerService;
import com.freedomfm.singer.service.SongService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModeratorControllerTests extends SingerApplicationTests {
    @Autowired
    private SongService songService;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private SingerService singerService;
    private MockMultipartFile file;
    private long songId;
    private int rating = 3;

    @BeforeAll
    public void setup() {
        file = new MockMultipartFile(
                "song",
                "valid-audio.mp3",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[1024 * 1024 * 2]);

        Singer singer = singerService.create(
                "test-user@gmail.com",
                "test-user",
                "7181234567"
        );

        songId = songService.upload(file, singer);
    }

    @AfterAll
    public void cleanup() {
        cleanupTables();
    }

    @Test
    @Order(1)
    void givenAnonymousUser_whenGetAllSingers_thenUnauthorized() throws Exception {
        mvc.perform(get("/singers"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @Order(2)
    void givenAnonymousUser_whenGetSongRating_thenUnauthorized() throws Exception {
        mvc.perform(get("/songs/{song-id}", songId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @Order(3)
    void givenModerator_whenUpdateRating_thenOk() throws Exception {
        mvc.perform(put("/songs/{song-id}", songId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("MODERATORS"))
                                .jwt(jwt -> jwt.claim(
                                        StandardClaimNames.PREFERRED_USERNAME,
                                        "moderator@gmail.com")))
                        .param("rating", String.valueOf(rating)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
                .andDo(print());

        double newRating = songRepository.findById(songId)
                        .orElse(new Song())
                .getRating();

        assertNotEquals(0, newRating);
    }

    @Test
    @Order(4)
    void givenModerator_whenGetSongRating_thenOk() throws Exception {
        mvc.perform(get("/songs/{song-id}", songId)
                .with(jwt().authorities(new SimpleGrantedAuthority("MODERATORS"))
                        .jwt(jwt -> jwt.claim(
                                StandardClaimNames.PREFERRED_USERNAME,
                                "moderator@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
                .andDo(print());
    }

    @Test
    @Order(5)
    void givenModerator_whenGetAllSingers_thenAccessPermitted() throws Exception {
        mvc.perform(get("/singers")
                .with(jwt().authorities(new SimpleGrantedAuthority("MODERATORS"))
                        .jwt(jwt -> jwt.claim(
                                StandardClaimNames.PREFERRED_USERNAME,
                                "moderator@gmail.com"))))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
