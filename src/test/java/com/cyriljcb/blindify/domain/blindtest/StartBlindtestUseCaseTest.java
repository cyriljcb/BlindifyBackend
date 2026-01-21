package com.cyriljcb.blindify.domain.blindtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestConfigurationException;
import com.cyriljcb.blindify.domain.blindtest.port.BlindtestSessionRepository;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.music.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;

public class StartBlindtestUseCaseTest {
    private MusicCatalogPort catalogPort;
    private MusicTimePort timePort;
    private BlindtestSessionRepository sessionRepository;
    private TrackSelector trackSelector;

    private StartBlindtestUseCase useCase;


     @BeforeEach
    void setup() {
        catalogPort = mock(MusicCatalogPort.class);
        timePort = mock(MusicTimePort.class);
        sessionRepository = mock(BlindtestSessionRepository.class);
        trackSelector = new TrackSelector();

        useCase = new StartBlindtestUseCase(
            catalogPort,
            timePort,
            trackSelector,
            sessionRepository
        );
    }

    @Test
    void should_create_blindtest_when_inputs_are_valid(){
        List<Music> musics =new ArrayList<>(List.of(
            new Music("1111111","Track1",15000,null,null,null,null,null,null), 
            new Music("2222222","Track2",25000,null,null,null,null,null,null),
            new Music("3333333","Track3",35000,null,null,null,null,null,null)
        ));

        when(catalogPort.getMusicFromPlaylist("playlist")).thenReturn(musics);
        when(timePort.getRevealTimeSec()).thenReturn(10);
        when(timePort.getDiscoveryTimeSec()).thenReturn(20);

    
        assertDoesNotThrow(() ->
            useCase.start("playlist", 2)
        );
    }

    @Test
    void should_throw_an_exception_when_music_list_is_empty(){

         when(catalogPort.getMusicFromPlaylist("playlist")).thenReturn(List.of());

        InvalidBlindtestConfigurationException ex =
            assertThrows(
                InvalidBlindtestConfigurationException.class,
                () -> useCase.start("playlist", 2)
            );

        assertEquals("Blindtest requires at least one track", ex.getMessage());
    }

    @Test
    void should_throw_an_exception_when_nbrTrack_is_less_than_0(){
      InvalidBlindtestConfigurationException ex =
        assertThrows(
            InvalidBlindtestConfigurationException.class,
            () -> useCase.start("playlist", 0)
        );

    assertEquals(
        "Number of tracks must be greater than zero",
        ex.getMessage()
    );
    }
    
}
