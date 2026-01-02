package com.cyriljcb.blindify.domain.trackselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cyriljcb.blindify.domain.blindtesttrack.BlindtestTrack;
import com.cyriljcb.blindify.domain.music.Music;
import com.cyriljcb.blindify.domain.trackselector.exception.InvalidTrackSelectorException;

public class TrackSelector {
    public TrackSelector(){
        
    }
    public List<BlindtestTrack> createTrackPlaylist(List<Music> musics, int nbrTitle) throws InvalidTrackSelectorException{ //pas besoin de filtre pour le moment
        if (musics.size() == 0||nbrTitle <=0) {
            throw new InvalidTrackSelectorException("Liste ou le nombre de titre est à 0");
        }
        if (nbrTitle>musics.size())
            throw new InvalidTrackSelectorException("Le nombre de titre demandé dépasse la dimension de la liste");
        List<BlindtestTrack> listTrack = new ArrayList<>();
        List<Music> listMusics = new ArrayList<>(musics);
        Collections.shuffle(listMusics);
        for(int i = 0; i<nbrTitle;i++ )
        {
            
            listTrack.add(new BlindtestTrack(listMusics.get(i)));
        }
        return listTrack;
    }

}
