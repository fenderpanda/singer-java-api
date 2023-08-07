package com.freedomfm.singer.exception;

public class SingerAlreadyParticipatedException extends RuntimeException {
    public SingerAlreadyParticipatedException(long singerId) {
        super(String.format("User '%d' already participated", singerId));
    }
}
