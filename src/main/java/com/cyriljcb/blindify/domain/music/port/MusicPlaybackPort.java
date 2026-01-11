package com.cyriljcb.blindify.domain.music.port;

public interface MusicPlaybackPort {
    void play();
    void pause();
    void seekToSecond(int seconds);
}
