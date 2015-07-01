package secondapp.android.alexander.com.myapplication;

import java.util.ArrayList;

/**
 * Created by Alexander on 6/25/2015.
 */
public class Artist implements Comparable<Artist>{
    private String artist;
    ArrayList<Album> albums = new ArrayList<Album>();

    public Artist(String artist){
        this.artist = artist;
    }

    public String getArtist(){
        return this.artist;
    }

    public ArrayList getAlbums(){
        return this.albums;
    }

    @Override
    public String toString(){
        return this.artist;
    }

    @Override
    public int compareTo(Artist x){
        return (this.artist).compareTo(x.getArtist());
    }
}
