package com.cyriljcb.blindify.infrastructure.spotify.dto;

import java.util.List;

public class SpotifyTrackDto {
    
    public String id;
    public String name;
    public int duration_ms;
    public List<SpotifyArtistDto> artists;
    public SpotifyAlbumDto album;

}
