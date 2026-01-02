package com.cyriljcb.blindify.domain.blindtestsettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cyriljcb.blindify.domain.blindtestsettings.exception.InvalidBlindtestSettingsException;
import com.cyriljcb.blindify.domain.blindtestsettings.port.MusicTimePort;

public class BlindtestSettingsTest {
    private MusicTimePort timePort;

    @BeforeEach
    void setup(){
        timePort = mock(MusicTimePort.class);
    }
    @Test
    void should_create_blindtestsettings_when_inputs_are_valid(){
        
        when(timePort.getDiscoveryTimeSec()).thenReturn(10);
        when(timePort.getRevealTimeSec()).thenReturn(5);

        assertNotNull(new BlindtestSettings(timePort.getRevealTimeSec(),timePort.getDiscoveryTimeSec()));
    }

    @Test
    void should_throw_an_exception_when_discovertime_is_0(){
        when(timePort.getDiscoveryTimeSec()).thenReturn(0);
        when(timePort.getRevealTimeSec()).thenReturn(10);
        InvalidBlindtestSettingsException ex = assertThrows(InvalidBlindtestSettingsException.class,()->new BlindtestSettings(timePort.getRevealTimeSec(),timePort.getDiscoveryTimeSec()) );
        
        assertEquals(ex.getMessage(),"Les valeurs doivent être strictement positives");
    }
    @Test
    void should_throw_an_exception_when_revealtime_is_0(){
        when(timePort.getDiscoveryTimeSec()).thenReturn(10);
        when(timePort.getRevealTimeSec()).thenReturn(0);
        InvalidBlindtestSettingsException ex = assertThrows(InvalidBlindtestSettingsException.class,()->new BlindtestSettings(timePort.getRevealTimeSec(),timePort.getDiscoveryTimeSec()) );
        
        assertEquals(ex.getMessage(),"Les valeurs doivent être strictement positives");
    }
}
