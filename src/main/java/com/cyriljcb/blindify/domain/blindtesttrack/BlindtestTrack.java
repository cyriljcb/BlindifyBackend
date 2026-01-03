package com.cyriljcb.blindify.domain.blindtesttrack;

import com.cyriljcb.blindify.domain.music.Music;

public class BlindtestTrack {
    private boolean isPlayed = false;
    private Music music;

    public BlindtestTrack(Music music) throws InvalidBlindtestTrackException{
        if (music == null)
            throw new InvalidBlindtestTrackException("BlindtestTrack requires a valid music");
        this.music = music;
    }
    public void setPlayed(){
        this.isPlayed = true;
    }
}
