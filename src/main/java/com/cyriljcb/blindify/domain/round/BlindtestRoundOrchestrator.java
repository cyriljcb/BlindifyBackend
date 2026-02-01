package com.cyriljcb.blindify.domain.round;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;

public class BlindtestRoundOrchestrator implements RoundOrchestrator {

    private final BlindtestSessionRepository sessionRepository;
    private final MusicPlaybackPort playbackPort;
    private final MusicTimePort timePort;
    private final GameSchedulerPort scheduler;

    public BlindtestRoundOrchestrator(
            BlindtestSessionRepository sessionRepository,
            MusicPlaybackPort playbackPort,
            MusicTimePort timePort,
            GameSchedulerPort scheduler
    ) {
        this.sessionRepository = sessionRepository;
        this.playbackPort = playbackPort;
        this.timePort = timePort;
        this.scheduler = scheduler;
    }

    @Override
    public void start() {
        playNextRound();
    }

    private void playNextRound() {
        var blindtest = sessionRepository.getCurrent()
                .orElseThrow(NoActiveBlindtestException::new);

        if (blindtest.isFinished()) {
            playbackPort.pause();
            return;
        }

        var track = blindtest.getCurrentTrack();
        var discoveryTime = timePort.getDiscoveryTimeSec();
        var revealTime = timePort.getRevealTimeSec();

        // --- DISCOVERY ---
        blindtest.startDiscovery();
        playbackPort.playTrack(track.getMusic().getId());

        scheduler.schedule(discoveryTime, () -> {
            playbackPort.pause();

            // --- REVEAL ---
            blindtest.startReveal();

            int revealSecond =
                    track.computeRevealSecond(discoveryTime, revealTime);

            playbackPort.seekToSecond(revealSecond);
            playbackPort.resume();
        });

        scheduler.schedule(discoveryTime + revealTime, () -> {
            playbackPort.pause();

            blindtest.finishRound();
            track.markAsPlayed();
            blindtest.nextTrack();

            playNextRound();
        });
    }

    @Override
    public void stop() {
        playbackPort.pause();
    }
}
