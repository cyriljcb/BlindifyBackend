package com.cyriljcb.blindify.domain.blindtest;

import java.util.List;

import com.cyriljcb.blindify.domain.music.Music;

public interface MusicCatalogPort {
    public String[] getCatalog();
    public List<Music> getMusicFromPlaylist();
}
