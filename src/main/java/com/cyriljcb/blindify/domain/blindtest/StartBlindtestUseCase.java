package com.cyriljcb.blindify.domain.blindtest;

import java.util.List;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;
import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;

public class StartBlindtestUseCase {

    private final MusicCatalogPort catalogPort;
    private final MusicTimePort timePort;
    private final TrackSelector trackSelector;

    public StartBlindtestUseCase(MusicCatalogPort catalogPort,MusicTimePort timePort,TrackSelector trackSelector) {
        this.catalogPort = catalogPort;
        this.timePort = timePort;
        this.trackSelector = trackSelector;
    }

    public Blindtest start(int nbrTrack) {
        List<Music> musics = catalogPort.getMusicFromPlaylist();

        if (musics == null || musics.isEmpty())
            throw new InvalidBlindtestException("Liste de musique vide");

        int rvlTime = timePort.getRevealTimeSec();
        int dscvrTime = timePort.getDiscoveryTimeSec();

        BlindtestSettings settings =
            new BlindtestSettings(rvlTime, dscvrTime);

        List<BlindtestTrack> tracks =
            trackSelector.createTrackPlaylist(musics, nbrTrack);

        return new Blindtest(tracks, settings);
    }
}

