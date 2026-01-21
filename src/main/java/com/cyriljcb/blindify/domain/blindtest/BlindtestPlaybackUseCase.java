package com.cyriljcb.blindify.domain.blindtest;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindteststate.BlindtestState;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;

public class BlindtestPlaybackUseCase {

    private final MusicPlaybackPort playbackPort;
    private final BlindtestSessionRepository sessionRepository;

    public BlindtestPlaybackUseCase(
            MusicPlaybackPort playbackPort,
            BlindtestSessionRepository sessionRepository
    ) {
        this.playbackPort = playbackPort;
        this.sessionRepository = sessionRepository;
    }

    public void playCurrentTrack() {
        var blindtest = getActiveBlindtest();

        // Transition d'état implicite
        if (blindtest.getState() == BlindtestState.CREATED) {
            blindtest.start();
        } else if (blindtest.getState() == BlindtestState.PAUSED) {
            blindtest.resume();
        }

        var track = blindtest.getCurrentTrack();
        playbackPort.playTrack(track.getMusic().getId());
        track.markAsPlayed();
    }

    public void pause() {
        var blindtest = getActiveBlindtest();

        blindtest.pause();
        playbackPort.pause();
    }

    public void nextTrack() {
        var blindtest = getActiveBlindtest();

        blindtest.nextTrack();

        if (blindtest.isFinished()) {
            playbackPort.stop();
            return;
        }

        var track = blindtest.getCurrentTrack();
        playbackPort.playTrack(track.getMusic().getId());
        track.markAsPlayed();
    }

    private Blindtest getActiveBlindtest() {
        return sessionRepository.getCurrent()
                .orElseThrow(NoActiveBlindtestException::new);
    }
}
