package com.cyriljcb.blindify.domain.music;

public class InvalidMusicException extends RuntimeException {
    public InvalidMusicException (String s){
        super(s);
    }
}
