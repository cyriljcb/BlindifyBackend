package com.cyriljcb.blindify.domain.blindtest.fake;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.cyriljcb.blindify.domain.music.port.MusicPlaybackPort;

@Component
@Profile("test")
public class FakeMusicPlaybackAdapterTest implements MusicPlaybackPort {

    @Override
    public void playTrack(String trackId) {
        System.out.println("Play");
    }

    @Override
    public void pause() {}

    @Override
    public void seekToSecond(int sec) {}

    @Override
    public void stop() {}
}
