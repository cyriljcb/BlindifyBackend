package com.cyriljcb.blindify.domain.music.port;

public interface MusicPlaybackPort {
    void playTrack(String trackId);
    void pause();
    void stop();
    void seekToSecond(int seconds);
}
