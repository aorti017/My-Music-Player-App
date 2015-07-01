package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Alexander on 5/10/2015.
 */
public class functions{

    private MediaPlayer mp = null;
    private List<String> songList;
    private Context last_c;
    private String last_path_root = "";
    private String last_path = "";

    /*public List<String> updateFileList(final String path){
        List<String> file_list = new ArrayList<String>();
        File dire = new File(path);
        File[] files = dire.listFiles();
        if(files != null){
            for(File inFile : files){
                String str = inFile.getName();
                if(str.endsWith(".mp3") || str.endsWith(".flac") || str.endsWith(".m4a") || str.endsWith(".wav") || inFile.isDirectory()) {
                    file_list.add(str);
                }
            }
        }
        java.util.Collections.sort(file_list);
        return file_list;
    }

    public List<String> retrieveSongs(String path, List<String> file_list){
        File dire = new File(path);
        File[] files = dire.listFiles();
        if(files != null){
            for(File inFile : files){
                String str = inFile.getName();
                if(str.endsWith(".mp3") || str.endsWith(".flac") || str.endsWith(".m4a") || str.endsWith(".wav") || str.endsWith(".wav") || inFile.isDirectory()) {
                    if(!inFile.isDirectory()) {
                        file_list.add(path+"/"+str);
                    }
                    else {
                        String tmp = inFile.getAbsolutePath();
                        file_list = retrieveSongs(tmp, file_list);
                    }
                }
            }
        }
        java.util.Collections.sort(file_list);
        return file_list;
    }*/

    /*private String getNext(String path_root, String path, boolean shuffle){
        List<String> file_list = new ArrayList<String>();
        file_list = retrieveSongs(path_root, file_list);
        String next_path="";
        path = path.replace(next_path, "");
        int pos = file_list.indexOf(path);
        if(shuffle){
            Random rand = new Random(System.currentTimeMillis());
            pos = rand.nextInt(file_list.size() + 1);
            next_path = file_list.get(pos);
        }
        else if (pos == file_list.size()-1) {
            next_path = file_list.get(0);
        }
        else {
            next_path = file_list.get(pos + 1);
        }
        return next_path;
    }*/

    public void play(final String current_song_path, final Context c, final boolean shuffle, final List<Song> song_list){
        if(mp != null && mp.isPlaying()) {
            mp.stop();
            mp.reset();
            mp = MediaPlayer.create(c, Uri.parse(current_song_path));
            mp.start();
        }
        else {
            if(mp != null) {
                mp.stop();
                mp.reset();
            }
            mp = MediaPlayer.create(c, Uri.parse(current_song_path));
            mp.start();

        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String new_next_song_path = "";
                if(!shuffle) {
                    Song s = null;
                    for(int i = 0; i < song_list.size(); i++){
                        if(song_list.get(i).getPath().equals(current_song_path)){
                            s = song_list.get(i);
                        }
                    }
                    for (int i = 0; i < song_list.size(); i++) {
                        if (song_list.get(i).getAlbum().equals(s.getAlbum()) && song_list.get(i).getTrackNum() == s.getTrackNum() + 1) {
                            new_next_song_path = song_list.get(i).getPath();
                            break;
                        }
                    }
                }
                else{
                    Random rand = new Random(System.currentTimeMillis());
                    new_next_song_path = song_list.get(rand.nextInt(song_list.size() + 1)).getPath();
                }
                play(new_next_song_path, last_c, shuffle, song_list);
            }
        });
        last_c = c;
        last_path = current_song_path;
    }

    public void pause(){
        if(mp != null && mp.isPlaying()){
            mp.pause();
        }
        else if(mp != null && !mp.isPlaying()){
            mp.start();
        }
    }

    public void next(final boolean shuffle, final List<Song> songs){
        if(mp != null){
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Song str = null;
                    for(int i = 0; i < songs.size(); i++){
                        if(songs.get(i).getPath().equals(last_path)){
                            str = songs.get(i);
                        }
                    }
                    String next = "";
                    if(!shuffle) {
                        for (int i = 0; i < songs.size(); i++) {
                            if (songs.get(i).getAlbum().equals(str.getAlbum()) && songs.get(i).getTrackNum() == str.getTrackNum() + 1) {
                                next = songs.get(i).getPath();
                                break;
                            }
                        }
                    }
                    else{
                        Random rand = new Random(System.currentTimeMillis());
                        next = songs.get(rand.nextInt(songs.size() + 1)).getPath();
                    }
                    play(next, last_c, shuffle, songs);
                }
            });
            mp.seekTo(mp.getDuration());
            if(!mp.isPlaying()){
                mp.start();
            }
        }
    }

    public void setNext(final boolean shuffle, final List<Song> songs){
        if(mp != null) {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Song str = null;
                    for(int i = 0; i < songs.size(); i++){
                        if(songs.get(i).getPath().equals(last_path)){
                            str = songs.get(i);
                        }
                    }
                    String next = "";
                    if(!shuffle) {
                        for (int i = 0; i < songs.size(); i++) {
                            if (songs.get(i).getAlbum().equals(str.getAlbum()) && songs.get(i).getTrackNum() == str.getTrackNum() + 1) {
                                next = songs.get(i).getPath();
                                break;
                            }
                        }
                    }
                    else{
                        Random rand = new Random(System.currentTimeMillis());
                        next = songs.get(rand.nextInt(songs.size() + 1)).getPath();
                    }
                    play(next, last_c, shuffle, songs);
                }
            });
        }
    }

    /*public void playPrev(){
    }*/
}
