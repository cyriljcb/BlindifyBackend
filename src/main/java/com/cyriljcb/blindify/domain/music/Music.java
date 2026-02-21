package com.cyriljcb.blindify.domain.music;

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
    private String imageUrl;
    
    public Music(String id,String name,int durationMs, String[] artistNames, String releaseYear, String imageUrl) throws InvalidMusicException{
        if (id==null || id.isBlank() ||name == null || name.isBlank())
            throw new InvalidMusicException("Music requires a valid id and a valid name");
        if(durationMs<=0)
            throw new InvalidMusicException("Music requires a duration strictly positive");
        this.id = id;
        this.name = name;
        this.artistNames = artistNames;
        this.durationMs = durationMs;
        this.releaseYear = releaseYear;
        this.imageUrl = imageUrl;
    }
    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.name;
    }
    public int getDurationMs(){
        return this.durationMs;
    }
    public String[] getArtistNames() {
         return artistNames; 
    }
    public String getPreviewUrl() {
        return previewUrl; 
    }
    public String getReleaseYear() { 
        return releaseYear; 
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
