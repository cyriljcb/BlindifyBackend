package com.cyriljcb.blindify.domain.music.port;

import java.util.List;

import com.cyriljcb.blindify.domain.music.Music;

public interface MusicCatalogPort {
    public List<Music> getMusicFromPlaylist(String playlistId);
}
