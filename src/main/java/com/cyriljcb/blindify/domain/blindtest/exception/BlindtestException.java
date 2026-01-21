package com.cyriljcb.blindify.domain.blindtest.exception;

public abstract class BlindtestException extends RuntimeException {
    protected BlindtestException(String message) {
        super(message);
    }
}
