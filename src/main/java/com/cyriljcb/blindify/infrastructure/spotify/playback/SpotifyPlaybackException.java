package com.cyriljcb.blindify.infrastructure.spotify.playback;

public class SpotifyPlaybackException extends RuntimeException {
    public SpotifyPlaybackException(String message, Throwable cause) {
        super(message, cause);
    }
}