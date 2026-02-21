package com.cyriljcb.blindify.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.domain.round.BlindtestRoundOrchestrator;
import com.cyriljcb.blindify.domain.round.RoundOrchestrator;
import com.cyriljcb.blindify.infrastructure.websocket.WebSocketEventPublisher;

@Configuration
public class RoundConfig {

    @Bean
    public RoundOrchestrator blindtestRoundOrchestrator(
            BlindtestSessionRepository sessionRepository,
            MusicPlaybackPort playbackPort,
            GameSchedulerPort scheduler,
            WebSocketEventPublisher eventPublisher 
    ) {
        return new BlindtestRoundOrchestrator(
                sessionRepository,
                playbackPort,
                scheduler,
                eventPublisher
        );
    }
}