package com.cyriljcb.blindify.infrastructure.spotify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyOAuthAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyMusicCatalogAdapter;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyHttpClient;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;

@Configuration
public class SpotifyConfig {

    @Value("${spotify.auth-url}")
    private String authUrl;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Bean
    public RestTemplate spotifyRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SpotifyAuthProvider spotifyAuthProvider(RestTemplate spotifyRestTemplate) {
        return new SpotifyOAuthAuthProvider(
                spotifyRestTemplate,
                authUrl,
                clientId,
                clientSecret
        );
    }

    @Bean
    public SpotifyClient spotifyClient(RestTemplate spotifyRestTemplate,
                                       SpotifyAuthProvider authProvider) {
        return new SpotifyHttpClient(
                spotifyRestTemplate,
                authProvider
        );
    }
    @Bean
    SpotifyMusicMapper spotifyMusicMapper() {
        return new SpotifyMusicMapper();
    }

    @Bean
    SpotifyMusicCatalogAdapter spotifyMusicCatalogAdapter(
            SpotifyClient spotifyClient,
            SpotifyMusicMapper mapper) {
        return new SpotifyMusicCatalogAdapter(spotifyClient, mapper);
    }
}
