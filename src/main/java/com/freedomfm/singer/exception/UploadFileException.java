package com.freedomfm.singer.exception;

public class UploadFileException extends RuntimeException{
    public UploadFileException(long singerId, String filename, Throwable ex) {
        super(String.format("UserId: '%d' couldn't upload file - '%s'", singerId, filename), ex);
    }
}
