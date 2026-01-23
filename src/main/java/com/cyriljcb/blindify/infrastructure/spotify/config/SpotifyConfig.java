package com.cyriljcb.blindify.infrastructure.spotify.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.cyriljcb.blindify.domain.blindtest.BlindtestPlaybackUseCase;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyAuthorizationCodeService;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyClientCredentialsAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.auth.SpotifyUserAuthProvider;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyMusicCatalogAdapter;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyHttpClient;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;
import com.cyriljcb.blindify.infrastructure.spotify.playback.SpotifyMusicPlaybackAdapter;

@Profile("spotify")
@Configuration
public class SpotifyConfig {

    @Value("${spotify.auth-url}")
    private String authUrl;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.api-base-url}")
    private String apiBaseUrl;

    // ------------------------------------------------------------------
    // Common
    // ------------------------------------------------------------------

    @Bean
    public RestTemplate spotifyRestTemplate() {
        return new RestTemplate();
    }

    // ------------------------------------------------------------------
    // AUTH PROVIDERS
    // ------------------------------------------------------------------

    /**
     * Used for Spotify Web API access (playlists, tracks, etc.)
     */
    @Bean
    @Qualifier("catalogAuth")
    public SpotifyAuthProvider spotifyCatalogAuthProvider(
            RestTemplate spotifyRestTemplate
    ) {
        return new SpotifyClientCredentialsAuthProvider(
                spotifyRestTemplate,
                authUrl,
                clientId,
                clientSecret
        );
    }

    /**
     * Used for Spotify playback (requires user authorization)
     */
    @Bean
    @Qualifier("userAuth")
    public SpotifyAuthProvider spotifyUserAuthProvider() {
        return new SpotifyUserAuthProvider();
    }

    // ------------------------------------------------------------------
    // CATALOG
    // ------------------------------------------------------------------

    @Bean
    public SpotifyClient spotifyClient(
            RestTemplate spotifyRestTemplate,
            @Qualifier("catalogAuth") SpotifyAuthProvider authProvider
    ) {
        return new SpotifyHttpClient(
                spotifyRestTemplate,
                authProvider
        );
    }

    @Bean
    public SpotifyMusicMapper spotifyMusicMapper() {
        return new SpotifyMusicMapper();
    }

    @Bean
    public SpotifyMusicCatalogAdapter spotifyMusicCatalogAdapter(
            SpotifyClient spotifyClient,
            SpotifyMusicMapper mapper
    ) {
        return new SpotifyMusicCatalogAdapter(spotifyClient, mapper);
    }

    // ------------------------------------------------------------------
    // PLAYBACK
    // ------------------------------------------------------------------

    @Bean
    public MusicPlaybackPort spotifyMusicPlaybackAdapter(
            RestTemplate spotifyRestTemplate,
            @Qualifier("userAuth") SpotifyAuthProvider authProvider
    ) {
        return new SpotifyMusicPlaybackAdapter(
                spotifyRestTemplate,
                authProvider,
                apiBaseUrl
        );
    }

    @Bean
    public BlindtestPlaybackUseCase blindtestPlaybackUseCase(
            MusicPlaybackPort playbackPort,
            BlindtestSessionRepository sessionRepository
    ) {
        return new BlindtestPlaybackUseCase(
                playbackPort,
                sessionRepository
        );
    }

    // ------------------------------------------------------------------
    // AUTHORIZATION CODE FLOW
    // ------------------------------------------------------------------

@Bean
public SpotifyAuthorizationCodeService spotifyAuthorizationCodeService(
        RestTemplate spotifyRestTemplate,
        @Qualifier("userAuth") SpotifyAuthProvider authProvider,
        @Value("${spotify.auth-url}") String authUrl,
        @Value("${spotify.client-id}") String clientId,
        @Value("${spotify.client-secret}") String clientSecret,
        @Value("${spotify.redirect-uri}") String redirectUri
) {
    return new SpotifyAuthorizationCodeService(
            spotifyRestTemplate,
            (SpotifyUserAuthProvider) authProvider,
            authUrl,
            clientId,
            clientSecret,
            redirectUri
    );
}


}
