package com.cyriljcb.blindify.infrastructure.web.dto;

public record StartBlindtestRequest(
    String playlistId,
    int tracks
) {}
