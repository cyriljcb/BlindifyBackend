package com.cyriljcb.blindify.domain.blindtest;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;

public class DefaultPlayBlindtestRoundUseCase
        implements PlayBlindtestRoundUseCase {

    private final MusicPlaybackPort playbackPort;
    private final MusicTimePort timePort;
    private final BlindtestSessionRepository sessionRepository;

    public DefaultPlayBlindtestRoundUseCase(
            MusicPlaybackPort playbackPort,
            MusicTimePort timePort,
            BlindtestSessionRepository sessionRepository
    ) {
        this.playbackPort = playbackPort;
        this.timePort = timePort;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void playCurrentRound() {

        var blindtest = sessionRepository.getCurrent()
                .orElseThrow(NoActiveBlindtestException::new);

        var track = blindtest.getCurrentTrack();

        // 1️⃣ Play
        playbackPort.playTrack(track.getMusic().getId());

        // 2️⃣ Discovery time
        sleepSeconds(timePort.getDiscoveryTimeSec());

        // 3️⃣ Pause
        playbackPort.pause();

        // 4️⃣ Reveal time
        sleepSeconds(timePort.getRevealTimeSec());

        // 5️⃣ Mark & move on
        track.markAsPlayed();
        blindtest.nextTrack();
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Round interrupted", e);
        }
    }
}
