package com.cyriljcb.blindify.infrastructure.spotify.mapper;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.music.InvalidMusicException;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyTrackDto;

public class SpotifyMusicMapper {

    public Music toDomain(SpotifyTrackDto dto) {

        try {
            String[] artistNames =
                dto.artists != null
                    ? dto.artists.stream()
                        .map(a -> a.name)
                        .toArray(String[]::new)
                    : new String[0];

            String releaseYear =
                dto.album != null && dto.album.release_date != null
                    ? dto.album.release_date.substring(0, 4)
                    : null;
            String imageUrl =
                dto.album != null &&
                dto.album.images != null &&
                !dto.album.images.isEmpty()
                    ? dto.album.images.get(0).url
                    : null;

            return new Music(
                dto.id,
                dto.name,
                dto.duration_ms,
                artistNames,
                releaseYear,
                imageUrl
            );

        } catch (InvalidMusicException e) {
            throw new RuntimeException(
                "Invalid Spotify track mapped to Music: " + dto.id,
                e
            );
        }
    }
}
