package com.cyriljcb.blindify.infrastructure.web.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthorizationCodeService;

@RestController
@RequestMapping("/auth/spotify")
public class SpotifyAuthCallbackController {

    private final SpotifyAuthorizationCodeService authService;
    
    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;

    public SpotifyAuthCallbackController(
            SpotifyAuthorizationCodeService authService
    ) {
        this.authService = authService;
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {
        
        authService.exchangeCodeForToken(code);
        response.sendRedirect(frontendUrl + "/playlists");
    }
}