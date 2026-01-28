package com.cyriljcb.blindify.domain.blindtesttrack;

import com.cyriljcb.blindify.domain.music.Music;

public class BlindtestTrack {

    private boolean played = false;
    private final Music music;

    public BlindtestTrack(Music music) {
        if (music == null) {
            throw new IllegalArgumentException("Music cannot be null");
        }
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

    public int getDurationMs() {
        return music.getDurationMs();
    }
    public int getDurationSec(){
        return music.getDurationMs()/1000;
    }
    /**
     * Règle métier :
     * - le reveal commence APRÈS la discovery
     * - jamais trop tôt
     * - jamais trop tard
     */
    public int computeRevealSecond(int discoveryTimeSec, int revealTimeSec) {

        int duration = getDurationSec();

        int minReveal = discoveryTimeSec;
        int maxReveal = duration - revealTimeSec;

        // cas morceau trop court
        if (maxReveal <= minReveal) {
            return duration / 2;
        }

        // ratio "musical" raisonnable
        int estimated = (int) (duration * 0.30);

        return Math.max(minReveal, Math.min(estimated, maxReveal));
    }
}
