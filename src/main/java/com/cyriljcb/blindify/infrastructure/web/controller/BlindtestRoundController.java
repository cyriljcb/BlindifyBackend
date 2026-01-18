package com.cyriljcb.blindify.infrastructure.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.domain.blindtest.PlayBlindtestRoundUseCase;

@RestController
@RequestMapping("/blindtest/round")
public class BlindtestRoundController {

    private final PlayBlindtestRoundUseCase roundUseCase;

    public BlindtestRoundController(
            PlayBlindtestRoundUseCase roundUseCase
    ) {
        this.roundUseCase = roundUseCase;
    }

    @PostMapping("/play")
    public void playRound() {
        roundUseCase.playCurrentRound();
    }
}
