package com.cyriljcb.blindify.infra.spotify.fake;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;

@Component
@Profile("test")
public class FakeSpotifyClientAdapter implements MusicCatalogPort {

    @Override
    public List<Music> getMusicFromPlaylist(String playlistId) {
        return List.of(
            new Music("1", "Fake Song 1", 180000, null, null,null),
            new Music("2", "Fake Song 2", 200000, null, null,null)
        );
    }
}