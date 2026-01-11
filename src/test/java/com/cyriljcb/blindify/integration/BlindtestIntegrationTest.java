package com.cyriljcb.blindify.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;
import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;

@SpringBootTest
@ActiveProfiles("test")
public class BlindtestIntegrationTest {

    @Autowired
    private StartBlindtestUseCase useCase;

    @Test
    void should_start_blindtest_with_fake_spotify() {
        Blindtest blindtest = useCase.start("fake-playlist", 2);

        assertNotNull(blindtest);
        assertEquals(2, blindtest.trackCount());
    }
}
