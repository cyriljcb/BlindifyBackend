package com.cyriljcb.blindify.infrastructure.spotify.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyTokenResponse {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("expires_in")
    public int expiresIn;

    @JsonProperty("refresh_token")
    public String refreshToken;
}
