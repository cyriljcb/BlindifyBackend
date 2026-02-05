package com.cyriljcb.blindify.infra.spotify.fake;

import java.util.List;

import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistTrackItemDto;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistTracksResponse;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyTrackDto;

public class FakeSpotifyResponses {
    public static SpotifyPlaylistTracksResponse oneTrack() {

        SpotifyTrackDto trackDto = new SpotifyTrackDto(); 
        trackDto.id = "track-1"; 
        trackDto.name = "Song 1"; 
        trackDto.duration_ms = 180000; 
        SpotifyPlaylistTrackItemDto itemDto = new SpotifyPlaylistTrackItemDto(); 
        itemDto.track = trackDto;

         SpotifyPlaylistTracksResponse response = new SpotifyPlaylistTracksResponse();
        response.items = List.of(itemDto);
        return response;
    }
}
