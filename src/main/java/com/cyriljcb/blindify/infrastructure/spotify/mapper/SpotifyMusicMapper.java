package com.cyriljcb.blindify.infrastructure.spotify.mapper;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyTrackDto;

public class SpotifyMusicMapper {
      public Music toDomain(SpotifyTrackDto dto) {
        return new Music(
            dto.id,
            dto.name,
            dto.duration_ms,
            null,   // artistNames 
            null,   // previewUrl
            null,   // artistId
            null,   // albumId
            null,   // genres
            null    // releaseYear
            //TODO à compléter
        );
    }
}
