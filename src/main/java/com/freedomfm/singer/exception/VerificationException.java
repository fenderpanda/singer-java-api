package com.freedomfm.singer.exception;

public class VerificationException extends RuntimeException{
    public VerificationException(long singerId, String code) {
        super(String.format("User '%d' couldn't pass verification with code '%s'", singerId, code));
    }
}
