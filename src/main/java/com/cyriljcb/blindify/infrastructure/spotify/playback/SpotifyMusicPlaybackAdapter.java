package com.cyriljcb.blindify.infrastructure.spotify.playback;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;

public class SpotifyMusicPlaybackAdapter implements MusicPlaybackPort {

    private final RestTemplate restTemplate;
    private final SpotifyAuthProvider authProvider;
    private final String apiBaseUrl;
    private static final String PLAYER_PATH = "/me/player";

    public SpotifyMusicPlaybackAdapter(
            RestTemplate restTemplate,
            SpotifyAuthProvider authProvider,
            String apiBaseUrl) {  
        this.restTemplate = restTemplate;
        this.authProvider = authProvider;
        this.apiBaseUrl = apiBaseUrl;
    }

    @Override
    public void playTrack(String trackId) {
        String url = apiBaseUrl +PLAYER_PATH+ "/play";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authProvider.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "uris", List.of("spotify:track:" + trackId)
        );

        HttpEntity<Map<String, Object>> entity =
            new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
            );
        } catch (RestClientException ex) {
            throw new SpotifyPlaybackException(
                "Failed to start playback for track " + trackId,
                ex
            );
        }
    }

    @Override
    public void pause() {
        sendPutRequest(apiBaseUrl +PLAYER_PATH + "/pause");
    }

    @Override
    public void stop() {
        pause();
    }

    @Override
    public void seekToSecond(int sec) {
        String url = apiBaseUrl + PLAYER_PATH + "/seek?position_ms=" + (sec * 1000);
        sendPutRequest(url);
    }

    private void sendPutRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authProvider.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
            );
        } catch (RestClientException ex) {
            throw new SpotifyPlaybackException("Spotify playback failed", ex);
        }
    }
}