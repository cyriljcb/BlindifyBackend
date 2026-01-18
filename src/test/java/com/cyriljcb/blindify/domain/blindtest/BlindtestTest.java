package com.cyriljcb.blindify.domain.blindtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cyriljcb.blindify.domain.blindtest.exception.InvalidBlindtestException;
import com.cyriljcb.blindify.domain.blindtestsettings.BlindtestSettings;
import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;
import com.cyriljcb.blindify.domain.music.Music;

public class BlindtestTest {
    BlindtestSettings blindtestsettings;
   
    @Test
    void should_create_blindtest_when_inputs_are_valid(){
        List<BlindtestTrack> tracks = List.of(
            new BlindtestTrack(new Music("1","Track1",15000,null,null,null,null,null,null)),
            new BlindtestTrack(new Music("2","Track2",25000,null,null,null,null,null,null))
        );
        blindtestsettings = new BlindtestSettings(10, 10);
        assertNotNull(new Blindtest(tracks, blindtestsettings));
    }

    @Test
    void should_throw_an_exception_when_blindtestTrack_is_null(){
        blindtestsettings = new BlindtestSettings(10, 10);

        InvalidBlindtestException ex = assertThrows(InvalidBlindtestException.class, ()->new Blindtest(null, blindtestsettings) );
        assertEquals("Blindtest requires at least one track and valid settings", ex.getMessage());
    }
    @Test
    void should_throw_an_exception_when_blindtestTrack_is_empty(){
        blindtestsettings = new BlindtestSettings(10, 10);
        List<BlindtestTrack> track = new ArrayList<>();
        InvalidBlindtestException ex = assertThrows(InvalidBlindtestException.class, ()->new Blindtest(track, blindtestsettings) );
        assertEquals("Blindtest requires at least one track and valid settings", ex.getMessage());
    }
    @Test
    void should_throw_an_exception_when_blindtestSettings_is_null(){
        List<BlindtestTrack> tracks = List.of(
            new BlindtestTrack(new Music("1","Track1",15000,null,null,null,null,null,null)),
            new BlindtestTrack(new Music("2","Track2",25000,null,null,null,null,null,null))
        );
         InvalidBlindtestException ex = assertThrows(InvalidBlindtestException.class, ()->new Blindtest(tracks,null));
        assertEquals("Blindtest requires at least one track and valid settings", ex.getMessage());
    }
}
