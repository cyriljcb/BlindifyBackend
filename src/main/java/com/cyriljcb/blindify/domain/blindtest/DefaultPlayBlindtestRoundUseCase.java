package com.cyriljcb.blindify.domain.blindtest;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;

public class DefaultPlayBlindtestRoundUseCase
        implements PlayBlindtestRoundUseCase {

    private final MusicPlaybackPort playbackPort;
    private final MusicTimePort timePort;
    private final BlindtestSessionRepository sessionRepository;
    private final GameSchedulerPort scheduler;

    public DefaultPlayBlindtestRoundUseCase(
            MusicPlaybackPort playbackPort,
            MusicTimePort timePort,
            BlindtestSessionRepository sessionRepository,
            GameSchedulerPort scheduler
    ) {
        this.playbackPort = playbackPort;
        this.timePort = timePort;
        this.sessionRepository = sessionRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void playCurrentRound() {

        var blindtest = sessionRepository.getCurrent()
                .orElseThrow(NoActiveBlindtestException::new);

        if (blindtest.isFinished()) {
            playbackPort.pause();
            return;
        }

        var track = blindtest.getCurrentTrack();
        var discoveryTime = timePort.getDiscoveryTimeSec();
        var revealTime = timePort.getRevealTimeSec();

        /* ======================
           DISCOVERY
           ====================== */
        blindtest.startDiscovery();
        playbackPort.playTrack(track.getMusic().getId());

        scheduler.schedule(
            discoveryTime,
            playbackPort::pause
        );

        /* ======================
           REVEAL
           ====================== */
        scheduler.schedule(
            discoveryTime + 1,
            () -> {
                blindtest.startReveal();

                int revealSecond =
                        track.computeRevealSecond(discoveryTime, revealTime);
                playbackPort.seekToSecond(revealSecond);
                playbackPort.resume();
            }
        );

        /* ======================
           FIN DU ROUND
           ====================== */
        scheduler.schedule(
            discoveryTime + revealTime,
            () -> {
                playbackPort.pause();

                blindtest.finishRound();
                track.markAsPlayed();
                blindtest.nextTrack();

                if (!blindtest.isFinished()) {
                    playCurrentRound();
                }
            }
        );
    }
}
