package com.cyriljcb.blindify.infrastructure.spotify.playback;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;

public class SpotifyMusicPlaybackAdapter implements MusicPlaybackPort {

    private final RestTemplate restTemplate;
    private final SpotifyAuthProvider authProvider;

    private static final String SPOTIFY_API_BASE_URL =
            "https://api.spotify.com/v1/me/player";

    public SpotifyMusicPlaybackAdapter(RestTemplate restTemplate,
                                       SpotifyAuthProvider authProvider) {
        this.restTemplate = restTemplate;
        this.authProvider = authProvider;
    }

    @Override
    public void startMusic() {
        sendPutRequest(SPOTIFY_API_BASE_URL + "/play");
    }

    @Override
    public void pauseMusic() {
        sendPutRequest(SPOTIFY_API_BASE_URL + "/pause");
    }

    @Override
    public void setTimeMusic(int sec) {
        String url = SPOTIFY_API_BASE_URL + "/seek?position_ms=" + (sec * 1000);
        sendPutRequest(url);
    }

    private void sendPutRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authProvider.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }
}
