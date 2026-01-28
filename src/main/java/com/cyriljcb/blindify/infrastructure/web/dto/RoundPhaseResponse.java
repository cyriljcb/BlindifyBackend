package com.cyriljcb.blindify.infrastructure.web.dto;

import com.cyriljcb.blindify.domain.roundphase.RoundPhase;

public record RoundPhaseResponse(RoundPhase phase) {
    public static RoundPhaseResponse from(RoundPhase phase) {
        return new RoundPhaseResponse(phase);
    }
}
