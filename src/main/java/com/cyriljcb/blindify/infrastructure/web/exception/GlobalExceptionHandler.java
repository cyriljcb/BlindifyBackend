package com.cyriljcb.blindify.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.cyriljcb.blindify.domain.blindtest.exception.NoActiveBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.exception.NoMoreTrackException;
import com.cyriljcb.blindify.infrastructure.spotify.catalog.SpotifyCatalogException;
import com.cyriljcb.blindify.infrastructure.web.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(InvalidBlindtestException.class)
    // public ResponseEntity<ApiErrorResponse> handleInvalidBlindtest(
    //         InvalidBlindtestException ex
    // ) {
    //     return ResponseEntity
    //         .badRequest()
    //         .body(ApiErrorResponse.of(
    //             400,
    //             "INVALID_BLINDTEST",
    //             ex.getMessage()
    //         ));
    // }

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
        ex.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorResponse.of(
                500,
                "INTERNAL_ERROR",
                "Unexpected error occurred"
            ));
    }
    @ExceptionHandler(NoActiveBlindtestException.class)
    public ResponseEntity<ApiErrorResponse> handleNoActiveBlindtest(NoActiveBlindtestException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT) // 409
            .body(ApiErrorResponse.of(
                409,
                "NO_ACTIVE_BLINDTEST",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(NoMoreTrackException.class)
    public ResponseEntity<ApiErrorResponse> handleNoMoreTrack(NoMoreTrackException ex) {
        return ResponseEntity
            .status(HttpStatus.GONE) // 410
            .body(ApiErrorResponse.of(
                410,
                "NO_MORE_TRACK",
                ex.getMessage()
            ));
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResource(NoResourceFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND) 
            .body(ApiErrorResponse.of(
                404,
                "NOT_FOUND",
                ex.getMessage()
            ));
    }


}
