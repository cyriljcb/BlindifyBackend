package com.cyriljcb.blindify.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.StartBlindtestRequest;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class BlindtestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_start_blindtest() {
        StartBlindtestRequest request =
            new StartBlindtestRequest("fake-playlist", 2);

        BlindtestResponse response =
            restTemplate.postForObject(
                "/blindtest/start",
                request,
                BlindtestResponse.class
            );
        assertNotNull(response);
        assertEquals(2, response.trackCount());
    }
}
