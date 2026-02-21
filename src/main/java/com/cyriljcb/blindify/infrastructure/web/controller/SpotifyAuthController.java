package com.cyriljcb.blindify.infrastructure.web.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthorizationCodeService;

@RestController
@RequestMapping("/auth/spotify")
public class SpotifyAuthController {    

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private final SpotifyAuthorizationCodeService authService;

    public SpotifyAuthController(SpotifyAuthorizationCodeService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public void redirectToSpotify(HttpServletResponse response)
            throws IOException {

        String url = UriComponentsBuilder
            .fromHttpUrl("https://accounts.spotify.com/authorize")
            .queryParam("client_id", clientId)
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam(
                "scope",
                "user-read-playback-state user-modify-playback-state playlist-read-private playlist-read-collaborative"
            )
            .build()
            .toUriString();

        response.sendRedirect(url);
    }
    @GetMapping("/status")
    public boolean isAuthenticated() {
        return authService.isAuthenticated();
    }
}
