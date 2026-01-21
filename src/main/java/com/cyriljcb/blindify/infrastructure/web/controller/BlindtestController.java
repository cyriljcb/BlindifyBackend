package com.cyriljcb.blindify.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.domain.blindtest.StartBlindtestUseCase;
import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestStateResponse;
import com.cyriljcb.blindify.infrastructure.web.dto.StartBlindtestRequest;

@RestController
@RequestMapping("/blindtest")
public class BlindtestController {

    private final StartBlindtestUseCase startBlindtestUseCase;
    private final BlindtestSessionRepository sessionRepository;

    public BlindtestController(StartBlindtestUseCase startBlindtestUseCase, BlindtestSessionRepository blindtestSessionRepository) {
        this.startBlindtestUseCase = startBlindtestUseCase;
        this.sessionRepository = blindtestSessionRepository;
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startBlindtest(@RequestBody StartBlindtestRequest request) {
        startBlindtestUseCase.start(
            request.playlistId(),
            request.tracks()
        );
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



}
