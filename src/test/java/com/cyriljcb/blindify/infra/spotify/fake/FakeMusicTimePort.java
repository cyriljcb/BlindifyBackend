package com.cyriljcb.blindify.infra.spotify.fake;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;

@Component
@Profile("test")
public class FakeMusicTimePort implements MusicTimePort {


    @Override
    public int getRevealTimeSec() {
        return 30;
    }

    @Override
    public int getDiscoveryTimeSec() {
        return 30;
    }
}
