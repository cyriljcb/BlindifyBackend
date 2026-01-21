package com.cyriljcb.blindify.domain.blindtest.exception;

public class NoActiveBlindtestException extends RuntimeException {
    public NoActiveBlindtestException() {
        super("No active blindtest session");
    }
    public NoActiveBlindtestException(String message) {
        super(message);
    }
}
