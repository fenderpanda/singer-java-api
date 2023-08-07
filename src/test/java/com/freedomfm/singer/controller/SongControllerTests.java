package com.freedomfm.singer.controller;

import com.freedomfm.singer.SingerApplicationTests;
import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.VerificationCode;
import com.freedomfm.singer.service.VerificationCodeService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongControllerTests extends SingerApplicationTests {
    @Autowired
    private VerificationCodeService codeService;
    private long singerId;
    private String code;
    private MockMultipartFile
            validFile,
            fileWithInvalidExtension,
            fileWithInvalidSize;

    @BeforeAll
    public void setup() {
        Singer singer = Singer.builder()
                .email("test-user@gmail.com")
                .name("test-user")
                .phone("7181234567")
                .build();

        VerificationCode verificationCode = codeService.create(singer);

        singerId = verificationCode.getSinger().getId();
        code = verificationCode.getCode();

        validFile = new MockMultipartFile(
                "song",
                "valid-audio.mp3",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[1024 * 1024 * 2]);

        fileWithInvalidExtension = new MockMultipartFile(
                "song",
                "file-with-wrong-extension.mpg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[1024 * 1024 * 2]);

        fileWithInvalidSize = new MockMultipartFile(
                "song",
                "valid-audio.mp3",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[1024 * 1024 * 31]);
    }

    @AfterAll
    public void cleanup() {
        cleanupTables();
    }

    @Test
    @Order(1)
    void givenFileWithInvalidExtension_whenUpload_thenBadRequest() throws Exception {
        mvc.perform(multipart("/songs")
                .file(fileWithInvalidExtension)
                .param("code", code)
                .param("singerId", String.valueOf(singerId)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException);
                })
                .andDo(print());
    }

    @Test
    @Order(2)
    void givenFileWithInvalidSize_whenUpload_thenBadRequest() throws Exception {
        mvc.perform(multipart("/songs")
                        .file(fileWithInvalidSize)
                        .param("code", code)
                        .param("singerId", String.valueOf(singerId)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof ConstraintViolationException);
                })
                .andDo(print());
    }

    @Test
    @Order(3)
    void givenValidFile_whenUpload_thenOk() throws Exception {
        mvc.perform(multipart("/songs")
                .file(validFile)
                .param("code", code)
                .param("singerId", String.valueOf(singerId)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
