package com.freedomfm.singer.exception;

public class FailedCodeVerificationException extends RuntimeException {
    public FailedCodeVerificationException(long singerId) {
        super(String.format("Singer with id = '%d' failed code verification", singerId));
    }
}
