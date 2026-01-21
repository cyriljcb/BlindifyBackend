package com.cyriljcb.blindify.domain.blindtest.exception;

public class NoMoreTrackException extends BlindtestException {
    public NoMoreTrackException() {
        super("No more track to play");
    }
}