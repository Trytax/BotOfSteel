package com.omega.audio.grabber;

public class GrabberException extends Exception {

    public GrabberException(String message) {
        super(message);
    }

    public GrabberException(String message, Throwable t) {
        super(message, t);
    }
}
