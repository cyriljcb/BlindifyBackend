package com.cyriljcb.blindify.domain.blindtest;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;
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

        // 1️⃣ Discovery : début du morceau
        playbackPort.playTrack(track.getMusic().getId());

        // 2️⃣ Pause après discovery
        scheduler.schedule(
            discoveryTime,
            playbackPort::pause
        );

        // 3️⃣ Reveal : seek vers le "refrain" + play
        scheduler.schedule(
            discoveryTime + 1,
            () -> {
                int revealSecond = computeRevealSecond(track);
                playbackPort.seekToSecond(revealSecond);
                playbackPort.resume();
            }
        );

        // 4️⃣ Fin du reveal + round suivant
        scheduler.schedule(
            discoveryTime + revealTime,
            () -> {
                playbackPort.pause();
                track.markAsPlayed();
                blindtest.nextTrack();

                if (!blindtest.isFinished()) {
                    playCurrentRound();
                }
            }
        );
    }
    private int computeRevealSecond(BlindtestTrack track) {

        int duration = track.getDurationSec();

        int discoveryTime = timePort.getDiscoveryTimeSec();
        int revealTime = timePort.getRevealTimeSec();

        // règle métier :
        // - le reveal commence APRÈS la discovery
        // - jamais trop tôt
        // - jamais trop tard
        int minReveal = discoveryTime;
        int maxReveal = duration - revealTime;

        if (maxReveal <= minReveal) {
            return duration / 2;
        }

        // ratio "musical" raisonnable
        int estimated = (int) (duration * 0.30);

        return Math.max(minReveal, Math.min(estimated, maxReveal));
    }

}
