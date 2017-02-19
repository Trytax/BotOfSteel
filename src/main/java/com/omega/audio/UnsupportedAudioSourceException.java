package com.omega.audio;

public class UnsupportedAudioSourceException extends RuntimeException {

    public UnsupportedAudioSourceException(String message) {
        super(message);
    }

    public UnsupportedAudioSourceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
