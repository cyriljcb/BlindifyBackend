package com.cyriljcb.blindify.domain.blindtest;

import java.util.List;

import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;

public class Blindtest {
    private List<BlindtestTrack> tracks;
    private BlindtestSettings settings;
    public Blindtest(List<BlindtestTrack> musicsList, BlindtestSettings settings) throws InvalidBlindtestException{
        if(musicsList == null || musicsList.isEmpty() || settings == null)
            throw new InvalidBlindtestException("Blindtest requires at least one track and valid settings");
        this.tracks = musicsList;
        this.settings = settings;
    }
    public int trackCount() {
        return tracks.size();
    }
}
