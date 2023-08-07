package com.freedomfm.singer.controller;

import com.freedomfm.singer.constraint.PhoneNumber;
import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.entity.VerificationCode;
import com.freedomfm.singer.exception.SingerAlreadyParticipatedException;
import com.freedomfm.singer.service.EmailService;
import com.freedomfm.singer.service.SingerService;
import com.freedomfm.singer.service.VerificationCodeService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"http://localhost:3000", "https://smilingcatservice.com"})
@RequiredArgsConstructor
@Validated
public class RegisterController {
    private final SingerService singerService;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;

    @PostMapping("/register")
    public long register(
            @RequestParam @NotBlank @Email
            String email,
            @RequestParam @NotBlank
            String name,
            @RequestParam @NotBlank @PhoneNumber
            String phone
    ) {
        if (singerService.isRegistered(email)) {
            Singer singer = singerService.getSinger(email);

            if (singerService.isParticipated(singer.getId())) {
                throw new SingerAlreadyParticipatedException(singer.getId());
            } else {
                VerificationCode code = verificationCodeService.create(singer);
                emailService.sendVerificationCode(singer, code.getCode());

                return singer.getId();
            }
        }

        Singer singer = singerService.create(email, name, phone);
        VerificationCode code = verificationCodeService.create(singer);
        emailService.sendVerificationCode(singer, code.getCode());

        return singer.getId();
    }
}
