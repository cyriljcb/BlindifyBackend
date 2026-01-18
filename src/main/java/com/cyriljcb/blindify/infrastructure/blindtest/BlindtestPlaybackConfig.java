package com.cyriljcb.blindify.infrastructure.blindtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyriljcb.blindify.domain.blindtest.BlindtestPlaybackUseCase;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;

@Configuration
public class BlindtestPlaybackConfig {

    @Bean
    BlindtestPlaybackUseCase playBlindtestTrackUseCase(MusicPlaybackPort playbackPort,BlindtestSessionRepository sessionRepository) {
        return new BlindtestPlaybackUseCase(
                playbackPort,
                sessionRepository
        );
    }
}
