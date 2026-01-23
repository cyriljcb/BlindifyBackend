package com.cyriljcb.blindify.infrastructure.web.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;

@RestController
@RequestMapping("/debug/spotify")
@Profile("spotify")
public class SpotifyDebugController {

    private final MusicCatalogPort catalogPort;

    public SpotifyDebugController(MusicCatalogPort catalogPort) {
        this.catalogPort = catalogPort;
    }

    @GetMapping("/playlist/{id}")
    public List<Music> getPlaylist(@PathVariable String id) {
        return catalogPort.getMusicFromPlaylist(id);
    }
}

