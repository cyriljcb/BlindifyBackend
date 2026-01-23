package com.cyriljcb.blindify.infrastructure.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthorizationCodeService;

@RestController
@RequestMapping("/auth/spotify")
public class SpotifyAuthCallbackController {

    private final SpotifyAuthorizationCodeService authService;

    public SpotifyAuthCallbackController(
            SpotifyAuthorizationCodeService authService
    ) {
        this.authService = authService;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        
        authService.exchangeCodeForToken(code);

        return "Spotify authentication successful. You can close this page.";
    }
}

