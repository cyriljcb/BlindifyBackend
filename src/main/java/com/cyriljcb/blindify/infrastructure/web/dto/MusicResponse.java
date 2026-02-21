package com.cyriljcb.blindify.infrastructure.web.dto;

public record MusicResponse(
    String id,
    String name,
    String[] artistNames,
    int durationMs,
    String releaseYear,
    String imageUrl
) {}