package com.cyriljcb.blindify.domain.blindtest;

import java.util.List;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestConfigurationException;
import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestStateException;
import com.cyriljcb.blindify.domain.blindtest.exception.NoMoreTrackException;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindteststate.BlindtestState;
import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;

public class Blindtest {

    private final List<BlindtestTrack> tracks;
    private final BlindtestSettings settings;
    private int currentIndex = 0;
    private BlindtestState state = BlindtestState.CREATED;

    public Blindtest(List<BlindtestTrack> tracks,
                     BlindtestSettings settings) {

        if (tracks == null || tracks.isEmpty() || settings == null) {
            throw new InvalidBlindtestConfigurationException(
                "Blindtest requires at least one track and valid settings"
            );
        }

        this.tracks = tracks;
        this.settings = settings;
    }

    public BlindtestState getState() {
        return state;
    }

     public BlindtestTrack getCurrentTrack() {
        if (state == BlindtestState.FINISHED) {
            throw new InvalidBlindtestStateException("Blindtest is finished");
        }
        return tracks.get(currentIndex);
    }

    public void start() {
        if (state != BlindtestState.CREATED) {
            throw new InvalidBlindtestStateException("Blindtest already started");
        }
        state = BlindtestState.PLAYING;
    }

    public void pause() {
        if (state != BlindtestState.PLAYING) {
            throw new InvalidBlindtestStateException("Cannot pause now");
        }
        state = BlindtestState.PAUSED;
    }

    public void resume() {
        if (state != BlindtestState.PAUSED) {
            throw new InvalidBlindtestStateException("Cannot resume now");
        }
        state = BlindtestState.PLAYING;
    }

    public void nextTrack() {
        if (state == BlindtestState.FINISHED) {
        throw new NoMoreTrackException();
    }
        currentIndex++;
        if (currentIndex >= tracks.size()) {
            state = BlindtestState.FINISHED;
        }
    }

    public boolean isFinished() {
        return state == BlindtestState.FINISHED;
    }
    
    public int getTrackCount() {
        return tracks.size();
    }
    public int getCurrentIndex() {
        return currentIndex;
    }
}
