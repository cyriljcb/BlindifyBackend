package com.cyriljcb.blindify.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cyriljcb.blindify.infrastructure.web.dto.ApiErrorResponse;
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

        ResponseEntity<Void> response =
            restTemplate.postForEntity(
                "/blindtest/start",
                request,
                Void.class
            );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void should_return_400_when_tracks_is_invalid() {
        StartBlindtestRequest request =
            new StartBlindtestRequest("fake-playlist", 0);

        ResponseEntity<ApiErrorResponse> response =
            restTemplate.postForEntity(
                "/blindtest/start",
                request,
                ApiErrorResponse.class
            );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_BLINDTEST", response.getBody().error());
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_BLINDTEST", response.getBody().error());
    }

}
