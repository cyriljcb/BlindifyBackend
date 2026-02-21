package com.cyriljcb.blindify.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.domain.round.BlindtestRoundOrchestrator;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;
import com.cyriljcb.blindify.infrastructure.websocket.WebSocketEventPublisher;

@Configuration
public class BlindtestConfig {
     @Bean
    StartBlindtestUseCase startBlindtestUseCase(
            MusicCatalogPort catalogPort,
            TrackSelector trackSelector,
            BlindtestSessionRepository sessionRepository
    ) {
        return new StartBlindtestUseCase(
                catalogPort,
                trackSelector,
                sessionRepository
        );
    }
    @Bean
    BlindtestRoundOrchestrator roundOrchestrator(
            MusicPlaybackPort playbackPort,
            BlindtestSessionRepository sessionRepository,
            GameSchedulerPort schedulerPort,
            WebSocketEventPublisher eventPublisher 
    ) {
        return new BlindtestRoundOrchestrator(
                sessionRepository,
                playbackPort,
                schedulerPort,
                eventPublisher
        );
    }
}
