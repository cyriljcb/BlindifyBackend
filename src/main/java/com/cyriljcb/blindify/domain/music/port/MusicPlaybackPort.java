package com.cyriljcb.blindify.domain.music.port;

public interface MusicPlaybackPort {
    public void startMusic();
    public void pauseMusic();
    public void setTimeMusic(int sec);
}
