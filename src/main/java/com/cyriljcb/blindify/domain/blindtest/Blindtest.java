package com.cyriljcb.blindify.domain.blindtest;

import java.util.List;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestException;
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
            throw new InvalidBlindtestException(
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
            throw new InvalidBlindtestException("Blindtest is finished");
        }
        return tracks.get(currentIndex);
    }

    public void start() {
        if (state != BlindtestState.CREATED) {
            throw new InvalidBlindtestException("Blindtest already started");
        }
        state = BlindtestState.PLAYING;
    }

    public void pause() {
        if (state != BlindtestState.PLAYING) {
            throw new InvalidBlindtestException("Cannot pause now");
        }
        state = BlindtestState.PAUSED;
    }

    public void resume() {
        if (state != BlindtestState.PAUSED) {
            throw new InvalidBlindtestException("Cannot resume now");
        }
        state = BlindtestState.PLAYING;
    }

    public void nextTrack() {
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
