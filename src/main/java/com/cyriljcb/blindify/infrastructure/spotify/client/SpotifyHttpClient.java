package com.cyriljcb.blindify.infrastructure.spotify.client;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;

public class SpotifyHttpClient implements SpotifyClient{
    private final RestTemplate restTemplate;
    private final SpotifyAuthProvider authProvider;

    public SpotifyHttpClient(RestTemplate restTemplate,
                             SpotifyAuthProvider authProvider) {
        this.restTemplate = restTemplate;
        this.authProvider = authProvider;
    }

    @Override
    public SpotifyPlaylistResponse getPlaylistTracks(String playlistId) {

        String url =
            "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authProvider.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SpotifyPlaylistResponse.class
        ).getBody();
    }
}
