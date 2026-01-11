package com.cyriljcb.blindify.infrastructure.spotify.catalog;

import java.util.List;

import org.springframework.context.annotation.Profile;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;

@Profile("!test")
public class SpotifyMusicCatalogAdapter implements MusicCatalogPort{

    private final SpotifyClient spotifyClient;
    private final SpotifyMusicMapper musicMapper;

    public SpotifyMusicCatalogAdapter(SpotifyClient spotifyClient, SpotifyMusicMapper musicMapper){
        this.spotifyClient = spotifyClient;
        this.musicMapper = musicMapper;
    }

    @Override
    public List<Music> getMusicFromPlaylist(String playlistId){
        try {
            SpotifyPlaylistResponse response = spotifyClient.getPlaylistTracks(playlistId);
            return response.items.stream().map(item-> musicMapper.toDomain(item.track)).toList();
        } catch (Exception e) {
            throw new SpotifyCatalogException("Error accessing Spotify");
        }
    }
}
