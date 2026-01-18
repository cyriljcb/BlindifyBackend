package com.cyriljcb.blindify.domain.blindtest.exception;

public class NoMoreTrackException extends RuntimeException {
    public NoMoreTrackException() {
        super("No more track to play");
    }
}