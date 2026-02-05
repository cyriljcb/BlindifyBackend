package com.cyriljcb.blindify.infrastructure.spotify.mapper;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.playlist.Playlist;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistDto;

public class SpotifyPlaylistMapper {
      public Playlist toDomain(SpotifyPlaylistDto dto) {
        return new Playlist(
            dto.id,
            dto.name,
            dto.tracksCount,
            dto.imageUrl
        );
    }
}
