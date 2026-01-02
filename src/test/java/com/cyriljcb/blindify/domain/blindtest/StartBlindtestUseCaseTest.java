package com.cyriljcb.blindify.domain.blindtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestException;
import com.cyriljcb.blindify.domain.blindtest.port.MusicCatalogPort;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;
import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.trackselector.TrackSelector;
import com.cyriljcb.blindify.domain.trackselector.exception.InvalidTrackSelectorException;

public class StartBlindtestUseCaseTest {
    private MusicCatalogPort catalogPort;
    private MusicTimePort timePort;

    @BeforeEach
    void setup()
    {
        catalogPort = mock(MusicCatalogPort.class);
        timePort = mock(MusicTimePort.class);
    }

    @Test
    void should_create_blindtest_when_inputs_are_valid(){
        List<Music> musics =new ArrayList<>(List.of(
            new Music("1111111","Track1",15000,null,null,null,null,null,null), 
            new Music("2222222","Track2",25000,null,null,null,null,null,null),
            new Music("3333333","Track3",35000,null,null,null,null,null,null)
        ));

        when(catalogPort.getMusicFromPlaylist()).thenReturn(musics);
        when(timePort.getRevealTimeSec()).thenReturn(10);
        when(timePort.getDiscoveryTimeSec()).thenReturn(20);

    
        assertNotNull(
            new StartBlindtestUseCase(catalogPort, timePort, new TrackSelector())
        );
    }

    @Test
    void should_throw_an_exception_when_music_list_is_empty(){

        when(catalogPort.getMusicFromPlaylist()).thenReturn(List.of());
        when(timePort.getRevealTimeSec()).thenReturn(10);
        when(timePort.getDiscoveryTimeSec()).thenReturn(20);
        
        StartBlindtestUseCase useCase = new StartBlindtestUseCase(
                catalogPort, 
                timePort,
                new TrackSelector());
        
        
        InvalidBlindtestException ex = assertThrows(InvalidBlindtestException.class,
            () -> useCase.start(3));

        assertEquals("Liste de musique vide", ex.getMessage());
    }

    @Test
    void should_throw_an_exception_when_nbrTrack_is_less_than_0(){
     List<Music> musics =new ArrayList<>(List.of(
            new Music("1111111","Track1",15000,null,null,null,null,null,null), 
            new Music("2222222","Track2",25000,null,null,null,null,null,null),
            new Music("3333333","Track3",35000,null,null,null,null,null,null)
        ));
        when(catalogPort.getMusicFromPlaylist()).thenReturn(musics);
        when(timePort.getRevealTimeSec()).thenReturn(10);
        when(timePort.getDiscoveryTimeSec()).thenReturn(20);

        StartBlindtestUseCase useCase = new StartBlindtestUseCase(catalogPort, timePort,new TrackSelector());

        InvalidTrackSelectorException ex = assertThrows(InvalidTrackSelectorException.class, ()-> useCase.start(0));
        assertEquals("Liste ou le nombre de titre est à 0", ex.getMessage());
    }
    
}
