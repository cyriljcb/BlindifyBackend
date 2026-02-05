package com.cyriljcb.blindify.infrastructure.spotify.client;

import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistTracksResponse;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyUserPlaylistsResponse;

public interface SpotifyClient {
    public SpotifyPlaylistTracksResponse getPlaylistTracks(String playlistId);
    public SpotifyUserPlaylistsResponse getUserPlaylists();
}
