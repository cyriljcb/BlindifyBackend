package com.cyriljcb.blindify.infrastructure.spotify.client;

import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;

public interface SpotifyClient {
    public SpotifyPlaylistResponse getPlaylistTracks(String playlistId);
 
}
