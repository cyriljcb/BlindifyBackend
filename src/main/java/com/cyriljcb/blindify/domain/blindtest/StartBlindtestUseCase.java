package com.cyriljcb.blindify.domain.blindtest;


import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestConfigurationException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;

public class StartBlindtestUseCase {

    private final MusicCatalogPort catalogPort;
    private final MusicTimePort timePort;
    private final TrackSelector trackSelector;
    private final BlindtestSessionRepository sessionRepository;

    public StartBlindtestUseCase(
            MusicCatalogPort catalogPort,
            MusicTimePort timePort,
            TrackSelector trackSelector,
            BlindtestSessionRepository sessionRepository
    ) {
        this.catalogPort = catalogPort;
        this.timePort = timePort;
        this.trackSelector = trackSelector;
        this.sessionRepository = sessionRepository;
    }

    public void start(String playlistId, int nbrTrack) {
        if (nbrTrack <= 0) {
            throw new InvalidBlindtestConfigurationException(
                "Number of tracks must be greater than zero"
            );
        }

        var musics = catalogPort.getMusicFromPlaylist(playlistId);
        if (musics == null || musics.isEmpty()) {
            throw new InvalidBlindtestConfigurationException(
                "Blindtest requires at least one track"
            );
        }

        var settings = new BlindtestSettings(
            timePort.getRevealTimeSec(),
            timePort.getDiscoveryTimeSec()
        );

        var tracks = trackSelector.createTrackPlaylist(musics, nbrTrack);

        var blindtest = new Blindtest(tracks, settings);
        blindtest.start();
        sessionRepository.save(blindtest);
    }
}

