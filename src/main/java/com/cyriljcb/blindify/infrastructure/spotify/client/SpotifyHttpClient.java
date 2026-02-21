package com.cyriljcb.blindify.infrastructure.spotify.client;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistTracksResponse;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyUserPlaylistsResponse;

public class SpotifyHttpClient implements SpotifyClient{
    private final RestTemplate restTemplate;
    private final SpotifyAuthProvider authProvider;

    public SpotifyHttpClient(RestTemplate restTemplate,
                             SpotifyAuthProvider authProvider) {
        this.restTemplate = restTemplate;
        this.authProvider = authProvider;
    }

    @Override
    public SpotifyPlaylistTracksResponse getPlaylistTracks(String playlistId) 
    {

        int limit = 100;
        int offset = 0;
        SpotifyPlaylistTracksResponse fullResponse = new SpotifyPlaylistTracksResponse();

        while (true) {
            String url =
                "https://api.spotify.com/v1/playlists/" + playlistId +
                "/tracks?limit=" + limit + "&offset=" + offset;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authProvider.getAccessToken());

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            SpotifyPlaylistTracksResponse response =
                restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SpotifyPlaylistTracksResponse.class
                ).getBody();

            if (response == null || response.items == null) {
                break;
            }

            fullResponse.items.addAll(response.items);

            if (response.items.size() < limit) {
                break; // dernière page
            }

            offset += limit;
        }

        return fullResponse;
    }


    @Override
    public SpotifyUserPlaylistsResponse getUserPlaylists() {
        String url = "https://api.spotify.com/v1/me/playlists";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authProvider.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SpotifyUserPlaylistsResponse.class
        ).getBody();
    }
}
