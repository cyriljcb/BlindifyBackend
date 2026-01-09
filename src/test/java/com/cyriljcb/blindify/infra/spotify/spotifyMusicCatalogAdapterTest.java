package com.cyriljcb.blindify.infra.spotify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyMusicCatalogAdapter;
import com.cyriljcb.blindify.infrastructure.spotify.client.SpotifyClient;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistItemDto;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyTrackDto;
import com.cyriljcb.blindify.infrastructure.spotify.mapper.SpotifyMusicMapper;

public class spotifyMusicCatalogAdapterTest {
    SpotifyClient spotifyClient;
    SpotifyMusicCatalogAdapter adapter;
    SpotifyMusicMapper musicMapper;

    @BeforeEach
        void setUp() {
            spotifyClient = mock(SpotifyClient.class);
            musicMapper = mock(SpotifyMusicMapper.class);
            adapter = new SpotifyMusicCatalogAdapter(spotifyClient,musicMapper);
        }

    @Test
    public void should_create_spotifymusiccatalogadapter_when_inputs_are_valid(){
        SpotifyTrackDto trackDto = new SpotifyTrackDto();
        trackDto.id = "track-1";
        trackDto.name = "Song 1";
        trackDto.duration_ms = 180000;
        SpotifyPlaylistItemDto itemDto = new SpotifyPlaylistItemDto();
        itemDto.track = trackDto;

        SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();
        response.items = List.of(itemDto);


        when(spotifyClient.getPlaylistTracks("playlist-123"))
            .thenReturn(response);

        List<Music> musics = adapter.getMusicFromPlaylist("playlist-123");

        assertEquals(1, musics.size());
        verify(spotifyClient).getPlaylistTracks("playlist-123");
        verify(musicMapper).toDomain(trackDto);
    }

    @Test
    void should_return_empty_list_when_playlist_has_no_items() {
        SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();
        response.items = List.of();

        when(spotifyClient.getPlaylistTracks(anyString()))
            .thenReturn(response);

        List<Music> musics = adapter.getMusicFromPlaylist("playlist-123");

        assertTrue(musics.isEmpty());
    }

}
