package com.cyriljcb.blindify.infrastructure.spotify.auth;

import java.time.Instant;

public class SpotifyUserAuthProvider
        implements SpotifyAuthProvider {

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    @Override
    public String getAccessToken() {
        if (tokenExpired()) {
            refreshToken();
        }
        return accessToken;
    }

    private boolean tokenExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }

    private void refreshToken() {
        // POST /api/token
        // grant_type=refresh_token
    }

    public void storeTokens(String accessToken,
                            String refreshToken,
                            int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = Instant.now().plusSeconds(expiresIn - 30);
    }
    
    public boolean hasValidAccessToken() {
        return accessToken != null && !tokenExpired();
    }

}


