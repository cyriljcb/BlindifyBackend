package com.cyriljcb.blindify.domain.round;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestFinishedEvent;
import com.cyriljcb.blindify.infrastructure.web.dto.PhaseEvent;
import com.cyriljcb.blindify.infrastructure.websocket.WebSocketEventPublisher;

public class BlindtestRoundOrchestrator implements RoundOrchestrator {

    private final BlindtestSessionRepository sessionRepository;
    private final MusicPlaybackPort playbackPort;
    private final GameSchedulerPort scheduler;
    private final WebSocketEventPublisher eventPublisher; 

    public BlindtestRoundOrchestrator(
            BlindtestSessionRepository sessionRepository,
            MusicPlaybackPort playbackPort,
            GameSchedulerPort scheduler,
            WebSocketEventPublisher eventPublisher 
    ) {
        this.sessionRepository = sessionRepository;
        this.playbackPort = playbackPort;
        this.scheduler = scheduler;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void start() {
        playNextRound();
    }

    private void playNextRound() {
        var blindtest = sessionRepository.getCurrent()
                .orElseThrow(NoActiveBlindtestException::new);

        if (blindtest.isFinished()) {
            //playbackPort.pause();
            System.out.println("BLINDTEST FINI");
            eventPublisher.publishBlindtestFinished(BlindtestFinishedEvent.create());
            return;
        }

        var track = blindtest.getCurrentTrack();
        var settings = blindtest.getSettings();
        var discoveryTime = settings.getDiscoveryTimeSec();
        var revealTime = settings.getRevealTimeSec();

        int currentRound = blindtest.getCurrentIndex() + 1;  
        int totalRounds = blindtest.getTrackCount();

        blindtest.startDiscovery();
        
        eventPublisher.publishPhaseChange(
            PhaseEvent.of(
                RoundPhase.DISCOVERY,
                track.getMusic().getId(),
                track.getMusic().getTitle(),
                track.getMusic().getArtistNames(),
                track.getMusic().getImageUrl(),
                discoveryTime,
                currentRound,   
                totalRounds 
            )
        );
        
        playbackPort.playTrack(track.getMusic().getId());

        scheduler.schedule(discoveryTime, () -> {
            playbackPort.pause();

            blindtest.startReveal();
            
            eventPublisher.publishPhaseChange(
                PhaseEvent.of(
                    RoundPhase.REVEAL,
                    track.getMusic().getId(),
                    track.getMusic().getTitle(),
                    track.getMusic().getArtistNames(),
                    track.getMusic().getImageUrl(),
                    revealTime,
                    currentRound,
                    totalRounds 
                )
            );

            int revealSecond = track.computeRevealSecond(discoveryTime, revealTime);
            playbackPort.seekToSecond(revealSecond);
            playbackPort.resume();
        });

        scheduler.schedule(discoveryTime + revealTime, () -> {
            playbackPort.pause();

            blindtest.finishRound();
            track.markAsPlayed();
             boolean wasLastTrack = (blindtest.getCurrentIndex() == blindtest.getTrackCount() - 1);
            blindtest.nextTrack();

             if (wasLastTrack) {
                eventPublisher.publishBlindtestFinished(BlindtestFinishedEvent.create());
                } else {
                    playNextRound();
                }
        });
    }

    @Override
    public void stop() {
        playbackPort.pause();
    }
}