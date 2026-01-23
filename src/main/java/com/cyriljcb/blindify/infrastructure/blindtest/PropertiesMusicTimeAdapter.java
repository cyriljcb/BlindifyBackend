package com.cyriljcb.blindify.infrastructure.blindtest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;

@Component
@Profile("spotify")
public class PropertiesMusicTimeAdapter implements MusicTimePort {

    @Value("${blindtest.discovery-time-sec:20}")
    private int discoveryTimeSec;

    @Value("${blindtest.reveal-time-sec:10}")
    private int revealTimeSec;

    @Override
    public int getRevealTimeSec() {
        return revealTimeSec;
    }

    @Override
    public int getDiscoveryTimeSec() {
        return discoveryTimeSec;
    }
}
