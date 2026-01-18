package com.cyriljcb.blindify.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.infrastructure.web.dto.StartBlindtestRequest;

@RestController
@RequestMapping("/blindtest")
public class BlindtestController {

    private final StartBlindtestUseCase startBlindtestUseCase;

    public BlindtestController(StartBlindtestUseCase startBlindtestUseCase) {
        this.startBlindtestUseCase = startBlindtestUseCase;
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startBlindtest(@RequestBody StartBlindtestRequest request) {
        startBlindtestUseCase.start(
            request.playlistId(),
            request.tracks()
        );
    }
}
