package com.freedomfm.singer.exception;

public class SendEmailException extends RuntimeException {
    public SendEmailException(Throwable ex) {
        super("Sending email error", ex);
    }
}
