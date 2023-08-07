package com.freedomfm.singer.exception;

public class SingerNotFoundException extends RuntimeException {
    public SingerNotFoundException(long singerId) {
        super(String.format("Singer with id: '%d' not found", singerId));
    }
}
