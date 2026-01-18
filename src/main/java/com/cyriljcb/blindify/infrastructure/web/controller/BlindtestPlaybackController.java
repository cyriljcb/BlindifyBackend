package com.cyriljcb.blindify.infrastructure.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.domain.blindtest.BlindtestPlaybackUseCase;

@RestController
@RequestMapping("/blindtest/playback")
public class BlindtestPlaybackController {

    private final BlindtestPlaybackUseCase playUseCase;

    public BlindtestPlaybackController(
            BlindtestPlaybackUseCase playUseCase
    ) {
        this.playUseCase = playUseCase;
    }

    @PostMapping("/play")
    public void play() {
        playUseCase.playCurrentTrack();
    }

    @PostMapping("/pause")
    public void pause() {
        playUseCase.pause();
    }

    @PostMapping("/next")
    public void next() {
        playUseCase.nextTrack();
    }
}
