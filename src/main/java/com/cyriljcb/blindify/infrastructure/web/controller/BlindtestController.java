package com.cyriljcb.blindify.infrastructure.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.round.RoundOrchestrator;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestStateResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.PlaylistResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.RoundPhaseResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.StartBlindtestRequest;

@RestController
@RequestMapping("/blindtest")
public class BlindtestController {

    private final StartBlindtestUseCase startBlindtestUseCase;
    private final BlindtestSessionRepository sessionRepository;
    private final RoundOrchestrator roundOrchestrator;
    private final SpotifyClient spotifyClient;

    public BlindtestController(
            StartBlindtestUseCase startBlindtestUseCase,
            BlindtestSessionRepository sessionRepository,
            RoundOrchestrator roundOrchestrator,
            SpotifyClient spotifyClient
    ) {
        this.startBlindtestUseCase = startBlindtestUseCase;
        this.sessionRepository = sessionRepository;
        this.roundOrchestrator = roundOrchestrator;
        this.spotifyClient = spotifyClient;
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startBlindtest(@RequestBody StartBlindtestRequest request) {
        startBlindtestUseCase.start(
            request.playlistId(),
            request.tracks()
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
        var response = spotifyClient.getUserPlaylists();
        
        return response.items.stream()
                .map(playlist -> new PlaylistResponse(
                    playlist.id,
                    playlist.name,
                    playlist.tracksCount,
                    playlist.imageUrl
                ))
                .toList();
    }
}