package com.cyriljcb.blindify.domain.round;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;
import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestFinishedEvent;
import com.cyriljcb.blindify.infrastructure.web.dto.PhaseEvent;
import com.cyriljcb.blindify.infrastructure.websocket.WebSocketEventPublisher;

public class BlindtestRoundOrchestrator implements RoundOrchestrator {

    private static final double FADE_DURATION_SEC = 0.2;

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
            eventPublisher.publishBlindtestFinished(BlindtestFinishedEvent.create());
            System.out.println("Blindtest terminé - la musique continue...");
            playbackPort.pause();
            return;
        }

        var track = blindtest.getCurrentTrack();
        var settings = blindtest.getSettings();
        var discoveryTime = settings.getDiscoveryTimeSec();
        var revealTime = settings.getRevealTimeSec();

        int currentRound = blindtest.getCurrentIndex() + 1;  
        int totalRounds = blindtest.getTrackCount();

        blindtest.startDiscovery();
        
        System.out.println("Round " + currentRound + "/" + totalRounds + " - Phase DISCOVERY");
        
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
            System.out.println(" Pause douce (fade-out)");
            playbackPort.pause();

            blindtest.startReveal();
            
            scheduler.schedule(FADE_DURATION_SEC, () -> {
                System.out.println(" Phase REVEAL");
                
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
        });

        scheduler.schedule(discoveryTime + FADE_DURATION_SEC + revealTime, () -> {
            System.out.println("Phase REVEAL terminée");

            blindtest.finishRound();
            track.markAsPlayed();
            boolean wasLastTrack = (blindtest.getCurrentIndex() == blindtest.getTrackCount() - 1);
            blindtest.nextTrack();

            if (wasLastTrack) {
                System.out.println("C'était le dernier morceau");
                scheduler.schedule(FADE_DURATION_SEC, () -> {
                    System.out.println("Publication de l'événement BlindtestFinished");
                    eventPublisher.publishBlindtestFinished(BlindtestFinishedEvent.create());
                });
            } else {
                System.out.println("Transition vers le morceau suivant (fade)");
                playbackPort.pause();

                scheduler.schedule(FADE_DURATION_SEC, () -> {
                    playNextRound();
                });
            }
        });
    }

    @Override
    public void stop() {
        playbackPort.pause();
    }
}