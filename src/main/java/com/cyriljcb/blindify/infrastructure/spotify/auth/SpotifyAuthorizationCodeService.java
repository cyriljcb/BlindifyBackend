package com.cyriljcb.blindify.infrastructure.spotify.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.dto.SpotifyTokenResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyAuthorizationCodeService {

    private final RestTemplate restTemplate;
    private final SpotifyUserAuthProvider authProvider;
    private final String authUrl;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public SpotifyAuthorizationCodeService(
            RestTemplate restTemplate,
            SpotifyUserAuthProvider authProvider,
            String authUrl,
            String clientId,
            String clientSecret,
            String redirectUri
    ) {
        this.restTemplate = restTemplate;
        this.authProvider = authProvider;
        this.authUrl = authUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public void exchangeCodeForToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        SpotifyTokenResponse response =
                restTemplate.postForObject(
                        authUrl,
                        request,
                        SpotifyTokenResponse.class
                );

        if (response == null || response.accessToken == null) {
            throw new IllegalStateException("Spotify token exchange failed");
        }
        log.info("authUrl={}", authUrl);
        log.info("clientId={}", clientId);
        log.info("redirectUri={}", redirectUri);

        authProvider.storeTokens(
                response.accessToken,
                response.refreshToken,
                response.expiresIn
        );
    }
        public boolean isAuthenticated() {
        return authProvider.hasValidAccessToken();
    }
}
