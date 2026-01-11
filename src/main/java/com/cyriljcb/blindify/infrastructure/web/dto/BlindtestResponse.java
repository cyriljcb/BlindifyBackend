package com.cyriljcb.blindify.infrastructure.web.dto;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;

public record BlindtestResponse(
    int trackCount
) {
    public static BlindtestResponse from(Blindtest blindtest) {
        return new BlindtestResponse(blindtest.trackCount());
    }
}
