package com.freedomfm.singer.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler({
            SingerNotFoundException.class,
            SongNotFoundException.class})
    public String handleSingerNotFoundException(Exception ex) {
        log.error(ex.getMessage());

        return getErrorMessage("error.internal");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(UploadFileException.class)
    public String handleUploadFileException(UploadFileException ex) {
        log.error(ex.getMessage(), ex.getCause());

        return getErrorMessage("error.internal");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SendEmailException.class)
    public String handleSendEmailException(SendEmailException ex) {
        log.error(ex.getMessage(), ex.getCause());

        return getErrorMessage("error.internal");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(VerificationException.class)
    public String handleVerificationException(VerificationException ex) {
        log.error(ex.getMessage());

        return getErrorMessage("error.internal");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SingerAlreadyParticipatedException.class)
    public String handleSingerAlreadyParticipatedException(SingerAlreadyParticipatedException ex) {
        log.error(ex.getMessage());

        return getErrorMessage("error.participated");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FailedCodeVerificationException.class)
    public String handleFailedCodeVerification(FailedCodeVerificationException ex) {
        log.error(ex.getMessage());

        return getErrorMessage("error.code-verification");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error(ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                getErrorMessage("error.register.label"),
                ex);

        List<SubError> subErrors = buildValidationErrors(ex.getConstraintViolations());
        apiError.setSubErrors(subErrors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private List<SubError> buildValidationErrors(Set<ConstraintViolation<?>> violations) {
        return violations
                .stream()
                .map(violation ->
                        ConstraintError.builder()
                                .field(FilenameUtils.getExtension(violation.getPropertyPath().toString()))
                                .message(violation.getMessage())
                                .build())
                .collect(Collectors.toList());
    }

    private String getErrorMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(message, null, locale);
    }
}
