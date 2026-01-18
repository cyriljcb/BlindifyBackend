package com.cyriljcb.blindify.domain.blindtesttrack;

import com.cyriljcb.blindify.domain.music.Music;

public class BlindtestTrack {
    private boolean played = false;
    private final Music music;

    public BlindtestTrack(Music music) throws InvalidBlindtestTrackException{
        if (music == null)
            throw new InvalidBlindtestTrackException("BlindtestTrack requires a valid music");
        this.music = music;
    }
     public boolean isPlayed() {
        return played;
    }

    public void markAsPlayed() {
        this.played = true;
    }

    public Music getMusic() {
        return music;
    }
}
