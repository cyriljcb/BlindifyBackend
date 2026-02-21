package com.cyriljcb.blindify.infrastructure.spotify.auth;

import java.time.Instant;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.dto.SpotifyTokenResponse;

public class SpotifyClientCredentialsAuthProvider implements SpotifyAuthProvider {

    private final RestTemplate restTemplate;
    private final String authUrl;
    private final String clientId;
    private final String clientSecret;

    private String cachedToken;
    private Instant tokenExpiration;

    public SpotifyClientCredentialsAuthProvider(RestTemplate restTemplate,
                                    String authUrl,
                                    String clientId,
                                    String clientSecret) {
        this.restTemplate = restTemplate;
        this.authUrl = authUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getAccessToken() {
        if (cachedToken == null || tokenExpired()) {
            fetchNewToken();
        }
        return cachedToken;
    }

    private boolean tokenExpired() {
        return tokenExpiration == null || Instant.now().isAfter(tokenExpiration);
    }

    private void fetchNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        SpotifyTokenResponse response =
                restTemplate.postForObject(
                        authUrl,
                        request,
                        SpotifyTokenResponse.class
                );

        if (response == null || response.accessToken == null) {
            throw new IllegalStateException("Failed to retrieve Spotify access token");
        }

        this.cachedToken = response.accessToken;
        this.tokenExpiration =
                Instant.now().plusSeconds(response.expiresIn - 30);
    }

    @Override
    public boolean hasValidAccessToken() {
        return cachedToken != null && !tokenExpired();
    }
}

