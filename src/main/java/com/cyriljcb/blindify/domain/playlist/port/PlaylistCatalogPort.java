package com.cyriljcb.blindify.domain.playlist.port;

import java.util.List;

import com.cyriljcb.blindify.domain.playlist.Playlist;

public interface PlaylistCatalogPort {
    List<Playlist> getUserPlaylists();
}
