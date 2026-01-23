package com.cyriljcb.blindify.infrastructure.spotify.playback;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;

public class SpotifyMusicPlaybackAdapter implements MusicPlaybackPort {

    private static final Logger log =
        LoggerFactory.getLogger(SpotifyMusicPlaybackAdapter.class);

    private static final String PLAYER_PATH = "/me/player";
    private static final String DEVICES_PATH = "/me/player/devices";

    private final RestTemplate restTemplate;
    private final SpotifyAuthProvider authProvider;
    private final String apiBaseUrl;

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

        String deviceId = resolveActiveDeviceId();

        String url = apiBaseUrl + PLAYER_PATH + "/play";
        if (deviceId != null) {
            url += "?device_id=" + deviceId;
        }

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
        } catch (HttpStatusCodeException ex) {
            log.error("Spotify PLAY failed [{}]: {}",
                ex.getStatusCode(),
                ex.getResponseBodyAsString()
            );
            throw new SpotifyPlaybackException(
                "Spotify refused playback: " + ex.getResponseBodyAsString(),
                ex
            );
        } catch (RestClientException ex) {
            log.error("Spotify PLAY failed", ex);
            throw new SpotifyPlaybackException(
                "Failed to start playback for track " + trackId,
                ex
            );
        }
    }

    @Override
    public void pause() {
        sendPutRequest(apiBaseUrl + PLAYER_PATH + "/pause");
    }

    @Override
    public void stop() {
        pause();
    }

    @Override
    public void seekToSecond(int sec) {
        String url = apiBaseUrl + PLAYER_PATH
            + "/seek?position_ms=" + (sec * 1000);
        sendPutRequest(url);
    }
    @Override
    public void resume() {
        sendPutRequest(apiBaseUrl + PLAYER_PATH + "/play");
    }


    // =========================================================
    //  Device handling
    // =========================================================

    private String resolveActiveDeviceId() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authProvider.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response =
                restTemplate.exchange(
                    apiBaseUrl + DEVICES_PATH,
                    HttpMethod.GET,
                    entity,
                    Map.class
                );

            List<Map<String, Object>> devices =
                (List<Map<String, Object>>) response
                    .getBody()
                    .get("devices");

            if (devices == null || devices.isEmpty()) {
                throw new SpotifyPlaybackException(
                    "No Spotify devices available. Open Spotify on a device.", null
                );
            }

            return devices.stream()
                .filter(d -> Boolean.TRUE.equals(d.get("is_active")))
                .map(d -> (String) d.get("id"))
                .findFirst()
                .orElse((String) devices.get(0).get("id"));

        } catch (Exception ex) {
            log.error("Failed to resolve Spotify device", ex);
            throw new SpotifyPlaybackException(
                "Unable to resolve Spotify playback device",
                ex
            );
        }
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
        } catch (HttpStatusCodeException ex) {
            log.error("Spotify PUT failed [{}]: {}",
                ex.getStatusCode(),
                ex.getResponseBodyAsString()
            );
            throw new SpotifyPlaybackException(
                ex.getResponseBodyAsString(),
                ex
            );
        } catch (RestClientException ex) {
            log.error("Spotify PUT failed", ex);
            throw new SpotifyPlaybackException(
                "Spotify playback failed",
                ex
            );
        }
    }
}
