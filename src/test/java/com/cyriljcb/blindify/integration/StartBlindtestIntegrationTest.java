package com.cyriljcb.blindify.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.domain.blindtest.Blindtest;
import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;
import com.cyriljcb.blindify.infra.spotify.fake.FakeSpotifyResponses;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyMusicCatalogAdapter;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyHttpClient;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;

public class StartBlindtestIntegrationTest {
     @Test
    void should_start_blindtest_with_spotify_catalog() {

        RestTemplate restTemplate = mock(RestTemplate.class);
        SpotifyAuthProvider authProvider = mock(SpotifyAuthProvider.class);

        SpotifyHttpClient spotifyClient =
                new SpotifyHttpClient(restTemplate, authProvider);

        SpotifyMusicMapper mapper = new SpotifyMusicMapper();

        MusicCatalogPort catalogPort =
                new SpotifyMusicCatalogAdapter(spotifyClient, mapper);

        MusicTimePort timePort = mock(MusicTimePort.class);
        when(timePort.getRevealTimeSec()).thenReturn(10);
        when(timePort.getDiscoveryTimeSec()).thenReturn(20);

        TrackSelector trackSelector = new TrackSelector();

        StartBlindtestUseCase useCase =
                new StartBlindtestUseCase(catalogPort, timePort, trackSelector);

        SpotifyPlaylistResponse response = FakeSpotifyResponses.oneTrack();

        when(authProvider.getAccessToken()).thenReturn("token");
        when(restTemplate.exchange(anyString(), any(), any(), eq(SpotifyPlaylistResponse.class)))
                .thenReturn(ResponseEntity.ok(response));

        Blindtest blindtest = useCase.start("playlist-123", 1);

        assertNotNull(blindtest);
        assertEquals(1, blindtest.trackCount());
    }

    
}
