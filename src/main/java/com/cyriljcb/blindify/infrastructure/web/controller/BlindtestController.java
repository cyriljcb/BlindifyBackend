package com.cyriljcb.blindify.infrastructure.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.round.RoundOrchestrator;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestStartedEvent;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestStateResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.MusicResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.PlaylistResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.RoundPhaseResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.StartBlindtestRequest;
import com.cyriljcb.blindify.infrastructure.web.mapper.MusicResponseMapper;
import com.cyriljcb.blindify.infrastructure.websocket.WebSocketEventPublisher;

@RestController
@RequestMapping("/blindtest")
public class BlindtestController {

    private final StartBlindtestUseCase startBlindtestUseCase;
    private final BlindtestSessionRepository sessionRepository;
    private final RoundOrchestrator roundOrchestrator;
    private final SpotifyClient spotifyClient;
    private final SpotifyMusicMapper spotifyMusicMapper;

    @Autowired
    private WebSocketEventPublisher eventPublisher;

    public BlindtestController(
        StartBlindtestUseCase startBlindtestUseCase,
        BlindtestSessionRepository sessionRepository,
        RoundOrchestrator roundOrchestrator,
        SpotifyClient spotifyClient,
        SpotifyMusicMapper spotifyMusicMapper
) {
    this.startBlindtestUseCase = startBlindtestUseCase;
    this.sessionRepository = sessionRepository;
    this.roundOrchestrator = roundOrchestrator;
    this.spotifyClient = spotifyClient;
    this.spotifyMusicMapper = spotifyMusicMapper;
}


    @PostMapping("/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startBlindtest(@RequestBody StartBlindtestRequest request) {
        BlindtestSettings settings = new BlindtestSettings(
        request.revealTimeSec(),
        request.discoveryTimeSec()
    );

        startBlindtestUseCase.start(
            request.playlistId(),
            request.tracks(),
            settings
        );

        eventPublisher.publishBlindtestStarted(
            BlindtestStartedEvent.of(request.tracks())
        );
        
        roundOrchestrator.start();
    }

    @GetMapping("/state")
    public BlindtestStateResponse state() {
        var blindtest = sessionRepository.getCurrent()
                .filter(bt -> !bt.isFinished())
                .orElseThrow(() ->
                    new NoActiveBlindtestException("No blindtest is currently active")
                );

        return BlindtestStateResponse.from(blindtest);
    }

    @GetMapping("/round-phase")
    public RoundPhaseResponse roundPhase() {
        var blindtest = sessionRepository.getCurrent()
                .orElseThrow(() ->
                    new NoActiveBlindtestException("No blindtest is currently active")
                );

        return RoundPhaseResponse.from(blindtest.getCurrentPhase());
    }

    @GetMapping("/playlists")
    public List<PlaylistResponse> getPlaylists() {
        System.out.println("🎵 [Backend] /playlists endpoint called");
        
        var response = spotifyClient.getUserPlaylists();
        
        System.out.println("🎵 [Backend] Spotify response received");
        System.out.println("🎵 [Backend] Response: " + response);
        System.out.println("🎵 [Backend] Items: " + (response != null ? response.items : "null"));
        System.out.println("🎵 [Backend] Items count: " + (response != null && response.items != null ? response.items.size() : 0));
        
        List<PlaylistResponse> playlists = response.items.stream()
                .map(playlist -> {
                    System.out.println("🎵 [Backend] Mapping playlist: " + playlist.name + " (id=" + playlist.id + ")");
                    System.out.println("   - tracksCount: " + playlist.tracks.total);
                    System.out.println("   - imageUrl: " + playlist.images.get(0).url);
                    
                    return new PlaylistResponse(
                        playlist.id,
                        playlist.name,
                        playlist.tracks != null ? playlist.tracks.total : 0,
                        playlist.images != null && !playlist.images.isEmpty()
                            ? playlist.images.get(0).url
                            : null
                    );
                })
                .toList();
        
        System.out.println("🎵 [Backend] Mapped playlists count: " + playlists.size());
        System.out.println("🎵 [Backend] Returning: " + playlists);
        
        return playlists;
    }
    @GetMapping("/playlists/{playlistId}/tracks")
    public List<MusicResponse> getPlaylistTracks(
            @PathVariable String playlistId
    ) {
        var response = spotifyClient.getPlaylistTracks(playlistId);

        return response.items.stream()
                .map(item -> spotifyMusicMapper.toDomain(item.track))
                .map(MusicResponseMapper::toResponse)
                .toList();
    }


}