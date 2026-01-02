package com.cyriljcb.blindify.domain.music;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.cyriljcb.blindify.domain.music.exception.InvalidMusicException;

public class MusicTest {
    @Test
    void should_create_Music_when_inputs_are_valid(){
        assertNotNull(new Music("1111111","Track1",15000,null,null,null,null,null,null)); 
    }
    
    @Test
    void should_throw_an_exception_when_id_is_empty(){ 
        InvalidMusicException ex = assertThrows(InvalidMusicException.class, ()->new Music("","Track1",15000,null,null,null,null,null,null));
        assertEquals("L'id ou le nom est vide", ex.getMessage());
    }
     @Test
    void should_throw_an_exception_when_name_is_empty(){ 
        InvalidMusicException ex = assertThrows(InvalidMusicException.class, ()->new Music("1111111","",15000,null,null,null,null,null,null));
        assertEquals("L'id ou le nom est vide", ex.getMessage());
    }
     @Test
    void should_throw_an_exception_when_duration_is_empty(){ 
        InvalidMusicException ex = assertThrows(InvalidMusicException.class, ()->new Music("1111111","Track1",0,null,null,null,null,null,null));
        assertEquals("la durée de la chanson doit être strictement positive", ex.getMessage());
    }
}
