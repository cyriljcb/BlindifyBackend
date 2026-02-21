package com.cyriljcb.blindify.infrastructure.web.dto;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;

public record BlindtestResponse(
    int trackCount,
    int currentIndex,
    String state
) {
    public static BlindtestResponse from(Blindtest blindtest) {
        return new BlindtestResponse(
            blindtest.getTrackCount(),
           blindtest.getCurrentIndex(),
            blindtest.getState().name()
        );
    }
}
