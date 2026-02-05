package com.cyriljcb.blindify.domain.playlist;

import com.cyriljcb.blindify.domain.music.InvalidMusicException;

public class Playlist {

    private final String id;
    private final String name;
    private final int tracksCount;
    private final String imageUrl;

    public Playlist(String id, String name, int tracksCount,String imageUrl){
         if (id==null || id.isBlank() ||name == null || name.isBlank())
            throw new InvalidMusicException("Playlist requires a valid id and a valid name");
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.tracksCount = tracksCount; 
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getTracksCount() {
        return this.tracksCount;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }   
}
