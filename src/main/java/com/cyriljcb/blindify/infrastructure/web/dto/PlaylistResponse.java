package com.cyriljcb.blindify.infrastructure.web.dto;

import com.cyriljcb.blindify.domain.playlist.Playlist;

public record PlaylistResponse(
        String id,
        String name,
        int tracksCount,
        String imageUrl
) {
    public static PlaylistResponse from(
            Playlist playlist
    ) {
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getName(),
                playlist.getTracksCount(),
                playlist.getImageUrl()
        );
    }
}
