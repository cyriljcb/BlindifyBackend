package com.cyriljcb.blindify.infra.spotify.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyHttpClient;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;

public class SpotifyHttpClientTest {

    private RestTemplate restTemplate;
    private SpotifyAuthProvider authProvider;
    private SpotifyHttpClient httpClient;
    @BeforeEach
    void setup(){
        restTemplate = mock(RestTemplate.class);
        authProvider = mock(SpotifyAuthProvider.class);
        httpClient = new SpotifyHttpClient(restTemplate, authProvider);    
    };

    @Test
    public void should_call_spotify_api_with_bearer_token_and_return_response(){
        
        SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();

        when(authProvider.getAccessToken()).thenReturn("fake-token");

        ResponseEntity<SpotifyPlaylistResponse> httpResponse =
                new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/playlists/playlist-123/tracks"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(SpotifyPlaylistResponse.class)
        )).thenReturn(httpResponse);
        
        SpotifyPlaylistResponse result = httpClient.getPlaylistTracks("playlist-123");
        
        assertSame(response, result);

        verify(authProvider,times(1)).getAccessToken();
        verify(restTemplate, times(1)).exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(SpotifyPlaylistResponse.class)
        );
    }
    
}
