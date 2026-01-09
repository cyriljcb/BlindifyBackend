package com.cyriljcb.blindify.infra.spotify.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyOAuthAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.auth.dto.SpotifyTokenResponse;

class SpotifyOAuthAuthProviderTest {

    
    private RestTemplate restTemplate;
    private SpotifyOAuthAuthProvider authProvider;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);

        authProvider = new SpotifyOAuthAuthProvider(
                restTemplate,
                "https://accounts.spotify.com/api/token",
                "client-id",
                "client-secret"
        );
    }

    @Test
    void should_return_access_token_when_spotify_returns_valid_response() {

        // GIVEN
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.accessToken = "fake-access-token";
        tokenResponse.expiresIn = 3600;

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(SpotifyTokenResponse.class)
        )).thenReturn(tokenResponse);

        // WHEN
        String token = authProvider.getAccessToken();

        // THEN
        assertEquals("fake-access-token", token);

        // Vérifie que Spotify n'est appelé qu'une fois
        verify(restTemplate, times(1))
                .postForObject(anyString(), any(HttpEntity.class), eq(SpotifyTokenResponse.class));
    }
}
