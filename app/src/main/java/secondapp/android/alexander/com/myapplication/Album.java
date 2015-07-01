package secondapp.android.alexander.com.myapplication;

import java.util.ArrayList;

/**
 * Created by Alexander on 6/25/2015.
 */
public class Album implements Comparable<Album>{
    private String album;
    private String artist;
    private ArrayList<Song> tracks = new ArrayList<Song>();

    public Album(String album, String artist){
        this.album = album;
        this.artist = artist;
    }

    public ArrayList getTracks(){
        return this.tracks;
    }

    public String getAlbum(){
        return this.album;
    }

    public String getArtist(){
        return this.artist;
    }

    @Override
    public String toString(){
        return getAlbum();
    }

    @Override
    public int compareTo(Album x){
        return (this.album).compareTo(x.getAlbum());
    }
}
