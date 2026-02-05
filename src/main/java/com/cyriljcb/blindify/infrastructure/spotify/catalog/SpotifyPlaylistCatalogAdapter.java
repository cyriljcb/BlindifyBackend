package com.cyriljcb.blindify.infrastructure.spotify.catalog;

import java.util.List;

import org.springframework.context.annotation.Profile;

import com.cyriljcb.blindify.domain.playlist.Playlist;
import com.cyriljcb.blindify.domain.playlist.port.PlaylistCatalogPort;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyPlaylistMapper;

@Profile("!test")
public class SpotifyPlaylistCatalogAdapter implements PlaylistCatalogPort {

    private final SpotifyClient spotifyClient;
    private final SpotifyPlaylistMapper playlistMapper;

    public SpotifyPlaylistCatalogAdapter(
            SpotifyClient spotifyClient,
            SpotifyPlaylistMapper playlistMapper
    ) {
        this.spotifyClient = spotifyClient;
        this.playlistMapper = playlistMapper;
    }

    @Override
    public List<Playlist> getUserPlaylists() {
        var response = spotifyClient.getUserPlaylists();
        return response.items.stream()
                .map(playlistMapper::toDomain)
                .toList();
    }
}