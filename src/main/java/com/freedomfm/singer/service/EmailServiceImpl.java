package com.freedomfm.singer.service;

import com.freedomfm.singer.config.EmailTemplateProperties;
import com.freedomfm.singer.entity.Singer;
import com.freedomfm.singer.exception.SendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${email-service.email}")
    private String senderEmail;
    @Value("${email-service.sender}")
    private String senderName;
    private final EmailTemplateProperties emailTemplateProperties;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final MessageSource messageSource;

    @Override
    public void sendVerificationCode(Singer singer, String verificationCode) {
        Locale locale = LocaleContextHolder.getLocale();

        var model = new HashMap<String, Object>();
        model.put("singer", singer);
        model.put("verificationCode", verificationCode);

        String to = singer.getEmail();

        String subject = messageSource.getMessage(
                "verification.subject",
                null,
                locale
        );

        Context thymeLeafContext = new Context(locale);
        thymeLeafContext.setVariables(model);
        String content = springTemplateEngine.process(
                "verification-template.html",
                thymeLeafContext);

        sendEmail(to, subject, content, null);
    }

    @Override
    public void sendCongratulation(Singer singer) {
        Locale locale = LocaleContextHolder.getLocale();

        var model = new HashMap<String, Object>();
        model.put("singer", singer);
        model.putAll(emailTemplateProperties.getLinks());

        String to = singer.getEmail();

        String subject = messageSource.getMessage(
                "congratulation.subject",
                null,
                locale
        );

        Context thymeLeafContext = new Context(locale);
        thymeLeafContext.setVariables(model);
        String content = springTemplateEngine.process(
                "congratulation-template.html",
                thymeLeafContext);

        sendEmail(to, subject, content, emailTemplateProperties.getLogos());
    }

    private void sendEmail(String to, String subject, String content, HashMap<String, String> logos) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(senderEmail, senderName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            if (logos != null && logos.size() > 0) {
                for (Map.Entry<String, String> set: logos.entrySet()) {
                    helper.addInline(set.getKey(), new ClassPathResource(set.getValue()));
                }
            }

            mailSender.send(message);
        } catch (MessagingException|UnsupportedEncodingException e) {
            throw new SendEmailException(e);
        }
    }
}
