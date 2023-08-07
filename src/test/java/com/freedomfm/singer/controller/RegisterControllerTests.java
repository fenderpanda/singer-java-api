package com.freedomfm.singer.controller;

import com.freedomfm.singer.SingerApplicationTests;
import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.Song;
import com.freedomfm.singer.entity.VerificationCode;
import com.freedomfm.singer.exception.SingerAlreadyParticipatedException;
import com.freedomfm.singer.repository.SingerRepository;
import com.freedomfm.singer.repository.SongRepository;
import com.freedomfm.singer.repository.VerificationCodeRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterControllerTests extends SingerApplicationTests {
    @Autowired
    private SingerRepository singerRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private VerificationCodeRepository codeRepository;
    private Singer correctSinger, incorrectSinger;

    @BeforeAll
    public void setup() {
        correctSinger = Singer.builder()
                .email("test-user@gmail.com")
                .name("test-user")
                .phone("7181234567")
                .build();

        incorrectSinger = Singer.builder()
                .email("test-user@")
                .name("")
                .phone("+17181234567")
                .build();
    }

    @AfterAll
    public void cleanup() {
        cleanupTables();
    }

    @Test
    @Order(1)
    void givenIncorrectSinger_whenRegister_thenBadRequest() throws Exception {
        mvc.perform(post("/register")
                .param("email", incorrectSinger.getEmail())
                .param("name", incorrectSinger.getName())
                .param("phone", incorrectSinger.getPhone()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException);
                })
                .andDo(print());
    }

    @Test
    @Order(2)
    void givenCorrectSinger_whenRegister_thenOk() throws Exception {
        mvc.perform(post("/register")
                .param("email", correctSinger.getEmail())
                .param("name", correctSinger.getName())
                .param("phone", correctSinger.getPhone()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
                .andDo(print());
    }

    @Test
    @Order(3)
    void givenExistUser_whenRegister_thenCodesAreNotEqual() throws Exception {
        VerificationCode code = codeRepository.findBySingerId(1L)
                        .orElse(null);

        mvc.perform(post("/register")
                        .param("email", correctSinger.getEmail())
                        .param("name", correctSinger.getName())
                        .param("phone", correctSinger.getPhone()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
                .andExpect(result -> {
                    VerificationCode changedCode = codeRepository.findBySingerId(1L)
                            .orElse(null);
                    assert changedCode != null;
                    assert code != null;
                    assertNotEquals(code.getCode(), changedCode.getCode());

                })
                .andDo(print());
    }

    @Test
    @Order(4)
    void givenParticipatedUser_whenRegister_thenBadRequest() throws Exception {
        Singer singer = singerRepository.findById(1L)
                .orElse(null);
        assert singer != null;
        Song song = new Song();
        song.setSinger(singer);
        songRepository.save(song);

        mvc.perform(post("/register")
                        .param("email", correctSinger.getEmail())
                        .param("name", correctSinger.getName())
                        .param("phone", correctSinger.getPhone()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof SingerAlreadyParticipatedException);
                })
                .andDo(print());
    }
}
