package com.cyriljcb.blindify.domain.blindtest;

import java.util.List;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestException;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;

public class Blindtest {
    private List<BlindtestTrack> tracks;
    private BlindtestSettings settings;
    public Blindtest(List<BlindtestTrack> musicsList, BlindtestSettings settings) throws InvalidBlindtestException{
        if(musicsList == null || musicsList.size() == 0 || settings == null)
            throw new InvalidBlindtestException("élément vide");
        this.tracks = musicsList;
        this.settings = settings;
    }
}
