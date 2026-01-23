package com.cyriljcb.blindify.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.domain.blindtest.PlayBlindtestRoundUseCase;
import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.infrastructure.web.dto.StartBlindtestRequest;

@RestController
@RequestMapping("/blindtest/game")
public class BlindtestGameController {

    private final PlayBlindtestRoundUseCase playBlindtestRoundUseCase;
    

    public BlindtestGameController(
            PlayBlindtestRoundUseCase playBlindtestRoundUseCase
    ) {
        this.playBlindtestRoundUseCase = playBlindtestRoundUseCase;
    }

    @PostMapping("/play")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void playBlindtest() {
        playBlindtestRoundUseCase.playCurrentRound();
    }
}