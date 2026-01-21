package com.cyriljcb.blindify.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.cyriljcb.blindify.domain.blindtest.exception.BlindtestException;
import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestConfigurationException;
import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.exception.NoMoreTrackException;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyCatalogException;
import com.cyriljcb.blindify.infrastructure.web.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
       BLINDTEST – MÉTIER
       ========================= */

    @ExceptionHandler(NoActiveBlindtestException.class)
    public ResponseEntity<ApiErrorResponse> handleNoActiveBlindtest(NoActiveBlindtestException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND) // 404
            .body(ApiErrorResponse.of(
                404,
                "NO_ACTIVE_BLINDTEST",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(NoMoreTrackException.class)
    public ResponseEntity<ApiErrorResponse> handleNoMoreTrack(NoMoreTrackException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT) // 409
            .body(ApiErrorResponse.of(
                409,
                "NO_MORE_TRACK",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(InvalidBlindtestConfigurationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidConfiguration(
            InvalidBlindtestConfigurationException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST) // 400
            .body(ApiErrorResponse.of(
                400,
                "INVALID_BLINDTEST_CONFIGURATION",
                ex.getMessage()
            ));
    }

    /**
     * Fallback métier blindtest
     */
    @ExceptionHandler(BlindtestException.class)
    public ResponseEntity<ApiErrorResponse> handleBlindtestException(BlindtestException ex) {
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
            .body(ApiErrorResponse.of(
                422,
                "BLINDTEST_ERROR",
                ex.getMessage()
            ));
    }

    /* =========================
       INFRASTRUCTURE
       ========================= */

    @ExceptionHandler(SpotifyCatalogException.class)
    public ResponseEntity<ApiErrorResponse> handleSpotifyError(SpotifyCatalogException ex) {
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
            .body(ApiErrorResponse.of(
                503,
                "SPOTIFY_UNAVAILABLE",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResource(NoResourceFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND) // 404
            .body(ApiErrorResponse.of(
                404,
                "NOT_FOUND",
                ex.getMessage()
            ));
    }

    /* =========================
       ERREUR TECHNIQUE
       ========================= */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
            .body(ApiErrorResponse.of(
                500,
                "INTERNAL_ERROR",
                "Unexpected error occurred"
            ));
    }
}
