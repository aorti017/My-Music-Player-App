package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 5/10/2015.
 */
public class mediaplayer extends Service {

    private MediaPlayer mp = null;
    private Context last_c;
    private String last_path = "";

    @Override
    public void onCreate(){ super.onCreate(); }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Bundle b = intent.getExtras();
        String current_song_path = null;
        String pause = null;
        Boolean shuffle = null;
        String next = null;
        String update = null;
        List<Song> song_list = null;
        if(b != null){
            current_song_path = b.getString("CURRENT_SONG_PATH");
            pause = b.getString("PAUSE");
            shuffle = b.getBoolean("SHUFFLE");
            update = b.getString("UPDATE");
            next = b.getString("NEXT");
            song_list = (List<Song>)intent.getSerializableExtra("SONG_LIST");
        }
        if(current_song_path != null) {
            play(current_song_path, getApplicationContext(), shuffle, song_list);
        }
        if(pause != null){
            pause();
        }
        if(next != null){
            next(shuffle, song_list);
        }
        if(update != null){
            setNext(shuffle, song_list);
        }
        /*if(prev != null){
            f.playPrev();
        }*/
        return Service.START_NOT_STICKY;
    }

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
                    for (int i = 0; i < songs.size(); i++) {
                        if (songs.get(i).getPath().equals(last_path)) {
                            str = songs.get(i);
                        }
                    }
                    String next = "";
                    if (!shuffle) {
                        for (int i = 0; i < songs.size(); i++) {
                            if (songs.get(i).getAlbum().equals(str.getAlbum()) && songs.get(i).getTrackNum() == str.getTrackNum() + 1) {
                                next = songs.get(i).getPath();
                                break;
                            }
                        }
                    } else {
                        Random rand = new Random(System.currentTimeMillis());
                        next = songs.get(rand.nextInt(songs.size() + 1)).getPath();
                    }
                    play(next, last_c, shuffle, songs);
                }
            });
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
