package com.cyriljcb.blindify.infrastructure.spotify.mapper;

import com.cyriljcb.blindify.domain.playlist.Playlist;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyImageDto;
import com.cyriljcb.blindify.infrastructure.spotify.dto.SpotifyPlaylistDto;

public class SpotifyPlaylistMapper {

    public Playlist toDomain(SpotifyPlaylistDto dto) {

        int tracksCount =
                dto.tracks != null
                        ? dto.tracks.total
                        : 0;

        String imageUrl =
                dto.images != null && !dto.images.isEmpty()
                        ? dto.images.get(0).url
                        : null;

        return new Playlist(
                dto.id,
                dto.name,
                tracksCount,
                imageUrl
        );
    }
}
