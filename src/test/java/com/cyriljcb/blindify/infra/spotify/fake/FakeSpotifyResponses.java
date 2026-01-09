package com.cyriljcb.blindify.infra.spotify.fake;

import java.util.List;

import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistItemDto;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistResponse;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyTrackDto;

public class FakeSpotifyResponses {
    public static SpotifyPlaylistResponse oneTrack() {

        SpotifyTrackDto trackDto = new SpotifyTrackDto(); 
        trackDto.id = "track-1"; 
        trackDto.name = "Song 1"; 
        trackDto.duration_ms = 180000; 
        SpotifyPlaylistItemDto itemDto = new SpotifyPlaylistItemDto(); 
        itemDto.track = trackDto;

         SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();
        response.items = List.of(itemDto);
        return response;
    }
}
