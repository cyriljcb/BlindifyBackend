package com.cyriljcb.blindify.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cyriljcb.blindify.domain.blindtest.InvalidBlindtestException;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyCatalogException;
import com.cyriljcb.blindify.infrastructure.web.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidBlindtestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidBlindtest(
            InvalidBlindtestException ex
    ) {
        return ResponseEntity
            .badRequest()
            .body(ApiErrorResponse.of(
                400,
                "INVALID_BLINDTEST",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(SpotifyCatalogException.class)
    public ResponseEntity<ApiErrorResponse> handleSpotifyError(
            SpotifyCatalogException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ApiErrorResponse.of(
                503,
                "SPOTIFY_UNAVAILABLE",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorResponse.of(
                500,
                "INTERNAL_ERROR",
                "Unexpected error occurred"
            ));
    }
}
