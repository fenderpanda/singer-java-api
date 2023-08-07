package com.freedomfm.singer.exception;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(long songId) {
        super(String.format("Song with id = '%d' not found", songId));
    }
}
