package com.cyriljcb.blindify.domain.music;

import com.cyriljcb.blindify.domain.music.exception.InvalidMusicException;

public class Music {
    private String id;
    private String name;
    private String[] artistNames;
    private String previewUrl;
    private int durationMs;
    private String artistId;
    private String albumId;       
    private String[] genres;     
    private String releaseYear; 
    
    public Music(String id,String name,int durationMs, String[] artistNames, String prevString,  String artistId, String albumId, String[] genres, String releaseYear) throws InvalidMusicException{
        if (id==null || id == "" ||name == null || name == "")
            throw new InvalidMusicException("L'id ou le nom est vide");
        if(durationMs<=0)
            throw new InvalidMusicException("la durée de la chanson doit être strictement positive");
        this.id = id;
        this.name = name;
        this.artistNames = artistNames;
        this.previewUrl = prevString;
        this.durationMs = durationMs;
        this.artistId = artistId;
        this.albumId = albumId;
        this.genres = genres;
        this.releaseYear = releaseYear;
    }
}
