package com.cyriljcb.blindify.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;

@SpringBootTest
@ActiveProfiles("test")
public class BlindtestIntegrationTest {

     @Autowired
    private StartBlindtestUseCase useCase;

    @Autowired
    private BlindtestSessionRepository sessionRepository;

    @Test
    void should_start_blindtest_with_fake_spotify() {
        BlindtestSettings settings = new BlindtestSettings(20, 20);
        useCase.start("fake-playlist", 2,settings);

        var blindtest = sessionRepository.getCurrent();

        assertNotNull(blindtest);
    }
}
