package com.cyriljcb.blindify.infrastructure.web.dto;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;

public record BlindtestStateResponse(
        String state,
        int currentTrackIndex,
        int trackCount,
        boolean finished
) {
    public static BlindtestStateResponse from(Blindtest blindtest) {
        return new BlindtestStateResponse(
                blindtest.getState().name(),
                blindtest.getCurrentIndex(),
                blindtest.getTrackCount(),
                blindtest.isFinished()
        );
    }
}
