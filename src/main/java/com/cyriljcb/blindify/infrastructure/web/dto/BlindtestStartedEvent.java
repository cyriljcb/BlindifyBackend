package com.cyriljcb.blindify.infrastructure.web.dto;

public record BlindtestStartedEvent(
    int totalTracks,
    long timestamp
) {
    public static BlindtestStartedEvent of(int totalTracks) {
        return new BlindtestStartedEvent(totalTracks, System.currentTimeMillis());
    }
}