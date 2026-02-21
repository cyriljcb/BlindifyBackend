package com.cyriljcb.blindify.infrastructure.web.dto;

import com.cyriljcb.blindify.domain.round.RoundPhase;

public record PhaseEvent(
    RoundPhase phase,
    String trackId,
    String trackName,
    String[] artists,
    String albumCoverUrl,
    int durationSeconds,
    int currentRound,  
    int totalRounds,      
    long timestamp
) {
    public static PhaseEvent of(
        RoundPhase phase, 
        String trackId, 
        String trackName,
        String[] artists,
        String albumCoverUrl,
        int durationSeconds,
        int currentRound,  
        int totalRounds     
    ) {
        return new PhaseEvent(
            phase,
            trackId,
            trackName,
            artists,
            albumCoverUrl,
            durationSeconds,
            currentRound,
            totalRounds,
            System.currentTimeMillis()
        );
    }
}