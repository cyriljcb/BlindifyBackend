package com.cyriljcb.blindify.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;
import com.cyriljcb.blindify.infra.spotify.fake.FakeSpotifyResponses;
import com.cyriljcb.blindify.infrastructure.blindtest.InMemoryBlindtestSessionRepository;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyMusicCatalogAdapter;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyHttpClient;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistTracksResponse;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;

public class StartBlindtestIntegrationTest {
    @Test
    void should_start_blindtest_with_spotify_catalog() {

        // Spotify infra
        RestTemplate restTemplate = mock(RestTemplate.class);
        SpotifyAuthProvider authProvider = mock(SpotifyAuthProvider.class);

        SpotifyHttpClient spotifyClient =
                new SpotifyHttpClient(restTemplate, authProvider);

        SpotifyMusicMapper mapper = new SpotifyMusicMapper();
        MusicCatalogPort catalogPort =
                new SpotifyMusicCatalogAdapter(spotifyClient, mapper);

        BlindtestSettings settings = new BlindtestSettings(20, 20);

        // Session
        BlindtestSessionRepository sessionRepository =
                new InMemoryBlindtestSessionRepository();

        // Use case
        StartBlindtestUseCase useCase =
                new StartBlindtestUseCase(
                        catalogPort,
                        new TrackSelector(),
                        sessionRepository
                );

        // Fake Spotify response
        SpotifyPlaylistTracksResponse response = FakeSpotifyResponses.oneTrack();
        when(authProvider.getAccessToken()).thenReturn("token");
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(SpotifyPlaylistTracksResponse.class)
        )).thenReturn(ResponseEntity.ok(response));

        // WHEN
        useCase.start("playlist-123", 1, settings);

        // THEN
        var blindtest = sessionRepository.getCurrent();

        assertNotNull(blindtest);
    }

    
}
