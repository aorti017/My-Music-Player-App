package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Alexander on 5/10/2015.
 */
public class functions{


    private MediaPlayer mp = null;
    private List<String> songList;
    private Context last_c;
    private String last_path_root = "";
    private String last_path = "";

    public List<String> updateFileList(final String path){
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
                if(str.endsWith(".mp3") || str.endsWith(".flac") || str.endsWith(".m4a") || str.endsWith(".wav") || inFile.isDirectory()) {
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
    }

    private String getNext(String path_root, String path, boolean shuffle){
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
    }

    public void play(String path, final Context c, final String path_root, final boolean shuffle){
        if(mp != null && mp.isPlaying()) {
            mp.stop();
            mp.reset();
            mp = MediaPlayer.create(c, Uri.parse(path));
            mp.start();
        }
        else {
            if(mp != null) {
                mp.stop();
                mp.reset();
            }
            mp = MediaPlayer.create(c, Uri.parse(path));
            mp.start();
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String next_path = getNext(last_path_root, last_path, shuffle);
                play(next_path, last_c, last_path_root, shuffle);
            }
        });
        last_c = c;
        last_path_root = path_root;
        last_path = path;
    }

    public void pause(){
        if(mp != null && mp.isPlaying()){
            mp.pause();
        }
        else if(mp != null && !mp.isPlaying()){
            mp.start();
        }
    }

    public void next(final boolean shuffle){
        if(mp != null){
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    String next_path = getNext(last_path_root, last_path, shuffle);
                    play(next_path, last_c, last_path_root, shuffle);
                }
            });
            mp.seekTo(mp.getDuration());
            if(!mp.isPlaying()){
                mp.start();
            }
        }
    }

    public void setNext(final boolean shuffle){
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
                public void onCompletion(MediaPlayer mp) {
                    String next_path = getNext(last_path_root, last_path, shuffle);
                    play(next_path, last_c, last_path_root, shuffle);
                }
            });
    }
}
