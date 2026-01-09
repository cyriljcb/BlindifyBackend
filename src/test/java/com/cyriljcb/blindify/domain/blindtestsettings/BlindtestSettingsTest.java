package com.cyriljcb.blindify.domain.blindtestsettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class BlindtestSettingsTest {
    @Test
    void should_create_blindtestsettings_when_inputs_are_valid(){
        

        assertNotNull(new BlindtestSettings(5,10));
    }

    @Test
    void should_throw_an_exception_when_discovertime_is_0(){
        InvalidBlindtestSettingsException ex = assertThrows(InvalidBlindtestSettingsException.class,()->new BlindtestSettings(10,0) );
        
        assertEquals(ex.getMessage(),"The durations must be strictly positive");
    }
    @Test
    void should_throw_an_exception_when_revealtime_is_0(){
        InvalidBlindtestSettingsException ex = assertThrows(InvalidBlindtestSettingsException.class,()->new BlindtestSettings(0,10) );
        
        assertEquals(ex.getMessage(),"The durations must be strictly positive");
    }
}
