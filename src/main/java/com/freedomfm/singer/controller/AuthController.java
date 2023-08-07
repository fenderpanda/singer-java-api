package com.freedomfm.singer.controller;

import com.freedomfm.singer.service.VerificationCodeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin({"http://localhost:3000", "https://smilingcatservice.com"})
public class AuthController {
    private final VerificationCodeService codeService;

    public AuthController(VerificationCodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/verify")
    public boolean verify(
            @RequestParam String code,
            @RequestParam long singerId
    ) {
        return codeService.verify(code, singerId);
    }
}
