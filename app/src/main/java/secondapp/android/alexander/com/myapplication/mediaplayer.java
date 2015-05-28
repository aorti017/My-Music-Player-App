package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

/**
 * Created by Alexander on 5/10/2015.
 */
public class mediaplayer extends Service {

    private functions f = new functions();

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Bundle b = intent.getExtras();
        String path = null;
        String pause = null;
        Boolean shuffle = null;
        String path_root = null;
        String next = null;
        String update = null;
        if(b != null){
            path = b.getString("PATH");
            pause = b.getString("PAUSE");
            shuffle = b.getBoolean("SHUFFLE");
            path_root = b.getString("PATH_ROOT");
            update = b.getString("UPDATE");
        }
        if(path != null) {
            f.play(path, getApplicationContext(), path_root, shuffle);
        }
        if(pause != null){
            f.pause();
        }
        if(next != null){
            f.next(shuffle);
        }
        if(update != null){
            f.setNext(shuffle);
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
