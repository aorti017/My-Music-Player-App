package secondapp.android.alexander.com.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Alexander on 6/25/2015.
 */
public class Song implements Comparable<Song>, Serializable{
    private String title;
    private String artist;
    private String album;
    private String path;
    private int trackNum;


    public Song(String title, String artist, String album, String path, int trackNum){
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.path = path;
        this.trackNum = trackNum;
    }

    public String getTitle(){
        return this.title;
    }

    public String getArtist(){
        return this.artist;
    }

    public String getAlbum(){
        return this.album;
    }

    public String getPath(){
        return this.path;
    }

    public int getTrackNum() { return this.trackNum; }

    @Override
    public String toString(){
        return getTitle();
    }

    @Override
    public int compareTo(Song x){

        if(this.trackNum < x.getTrackNum()){
            return -1;
        }
        else if(this.trackNum < x.getTrackNum()){
            return 1;
        }
        return 0;
    }
}
