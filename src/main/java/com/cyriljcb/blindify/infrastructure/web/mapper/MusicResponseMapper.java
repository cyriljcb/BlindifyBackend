package com.cyriljcb.blindify.infrastructure.web.mapper;

import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.infrastructure.web.dto.MusicResponse;

public class MusicResponseMapper {

    public static MusicResponse toResponse(Music music) {
        return new MusicResponse(
            music.getId(),
            music.getTitle(),
            music.getArtistNames(),
            music.getDurationMs(),
            music.getReleaseYear(),
            music.getImageUrl()
        );
    }
}

