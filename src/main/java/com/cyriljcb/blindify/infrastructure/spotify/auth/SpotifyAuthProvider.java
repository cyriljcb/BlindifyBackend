package com.cyriljcb.blindify.infrastructure.spotify.auth;

public interface SpotifyAuthProvider {
    public String getAccessToken();
    public boolean hasValidAccessToken();
}
