package com.cyriljcb.blindify.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;

@Configuration
public class BlindtestConfig {
     @Bean
    StartBlindtestUseCase startBlindtestUseCase(
            MusicCatalogPort catalogPort,
            MusicTimePort timePort,
            TrackSelector trackSelector,
            BlindtestSessionRepository sessionRepository
    ) {
        return new StartBlindtestUseCase(
                catalogPort,
                timePort,
                trackSelector,
                sessionRepository
        );
    }
}
