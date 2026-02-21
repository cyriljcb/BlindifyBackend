package com.cyriljcb.blindify.infrastructure.web.dto;

public record BlindtestFinishedEvent(
    long timestamp
) {
    public static BlindtestFinishedEvent create() {
        return new BlindtestFinishedEvent(System.currentTimeMillis());
    }
}