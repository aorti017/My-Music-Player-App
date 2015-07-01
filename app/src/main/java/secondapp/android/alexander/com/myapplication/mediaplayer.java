package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
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

/**
 * Created by Alexander on 5/10/2015.
 */
public class mediaplayer extends Service {

    private functions f = new functions();

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
            f.play(current_song_path, getApplicationContext(), shuffle, song_list);
        }
        if(pause != null){
            f.pause();
        }
        if(next != null){
            f.next(shuffle, song_list);
        }
        if(update != null){
            f.setNext(shuffle, song_list);
        }
        /*if(prev != null){
            f.playPrev();
        }*/
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
