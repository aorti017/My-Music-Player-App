package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;


public class MainActivity extends Activity{

    private boolean shuffle = false;
    private ArrayList<Song> songs = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int display = 2;
        String show_only_album = "";
        String show_only_artist = "";
        Bundle b = getIntent().getExtras();
        if(b != null){
            Boolean shuffle_check = b.getBoolean("SHUFFLE");
            Boolean bool_test = null;
            if(shuffle_check != bool_test){
                shuffle = shuffle_check;
            }

            Integer display_check = b.getInt("DISPLAY");
            Integer int_test = null;
            if(display_check != int_test){
                display = display_check;
            }

            String album_check = b.getString("ALBUM");
            if(album_check != null){
                show_only_album = album_check;
            }

            String artist_check = b.getString("ARTIST");
            if(artist_check != null){
                show_only_artist = artist_check;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.fileList);
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TRACK
        };
        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        if(display == 0) {
            while(cursor.moveToNext()) {
                String album = cursor.getString(0);
                if(show_only_album != ""){
                    if(album.equals(show_only_album)) {
                        songs.add(new Song(cursor.getString(2), cursor.getString(1), album, cursor.getString(3), cursor.getInt(4)));
                    }
                }
                else{
                    songs.add(new Song(cursor.getString(2), cursor.getString(1), cursor.getString(0),cursor.getString(3), cursor.getInt(4)));
                }
            }
            Collections.sort(songs);
            final ArrayAdapter<Song> arrayAdapter = new ArrayAdapter<Song>(
                    this,
                    android.R.layout.simple_list_item_1,
                    songs);
            lv.setAdapter(arrayAdapter);
        }
        else if(display == 1){
            while(cursor.moveToNext()) {
                if(show_only_artist != ""){
                    String artist = cursor.getString(1);
                    if(show_only_artist.equals(artist)){
                        albums.add(new Album(cursor.getString(0), artist));
                    }
                }
                else {
                    albums.add(new Album(cursor.getString(0), cursor.getString(1)));
                }
            }
            /*setting up album adapter*/
            List<Album> result_alb = new ArrayList<Album>();
            Set<String> albumSet = new HashSet<String>();
            for(Album a: albums){
                if(albumSet.add(a.getAlbum())){
                    result_alb.add(a);
                }
            }
            albums = result_alb;
            Collections.sort(albums);
            final ArrayAdapter<Album> arrayAdapter = new ArrayAdapter<Album>(
                    this,
                    android.R.layout.simple_list_item_1,
                    albums);
            lv.setAdapter(arrayAdapter);
        }
        else if(display == 2){
            while(cursor.moveToNext()) {
                artists.add(new Artist(cursor.getString(1)));
            }
            /*setting up artist adapter*/
            List<Artist> result = new ArrayList<Artist>();
            Set<String> artistSet = new HashSet<String>();
            for(Artist a: artists){
                if(artistSet.add(a.getArtist())){
                    result.add(a);
                }
            }
            artists = result;
            Collections.sort(artists);
            final ArrayAdapter<Artist> arrayAdapter = new ArrayAdapter<Artist>(
                    this,
                    android.R.layout.simple_list_item_1,
                    artists);
            lv.setAdapter(arrayAdapter);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                /*a series of try catch statements to preform specifc actions depending on the type
                of object that was clicked*/
                try {
                    Song str = (Song) adapter.getItemAtPosition(position);
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
                    Intent i = new Intent(getApplicationContext(), mediaplayer.class);
                    i.putExtra("CURRENT_SONG_PATH", str.getPath());
                    i.putExtra("SHUFFLE", shuffle);
                    i.putExtra("SONG_LIST", songs);
                    getApplicationContext().startService(i);
                } catch (Exception e){ }

                try {
                    Album str = (Album) adapter.getItemAtPosition(position);
                    Intent newIntent = new Intent(view.getContext(), MainActivity.class);
                    newIntent.putExtra("ALBUM", str.getAlbum());
                    newIntent.putExtra("SHUFFLE", shuffle);
                    newIntent.putExtra("DISPLAY", 0);
                    startActivityForResult(newIntent, 100);
                } catch (Exception e){ }

                try {
                    Artist str = (Artist) adapter.getItemAtPosition(position);
                    Intent newIntent = new Intent(view.getContext(), MainActivity.class);
                    newIntent.putExtra("ARTIST", str.getArtist());
                    newIntent.putExtra("SHUFFLE", shuffle);
                    newIntent.putExtra("DISPLAY", 1);
                    startActivityForResult(newIntent, 100);
                } catch (Exception e){ }
                /*if (new File(path + "/" + str).isDirectory()) {
                    Intent newIntent = new Intent(view.getContext(), MainActivity.class);
                    newIntent.putExtra("PATH", path + "/" + str);
                    newIntent.putExtra("SHUFFLE", shuffle);
                    startActivityForResult(newIntent, 100);
                } else {
                    Intent i = new Intent(getApplicationContext(), mediaplayer.class);
                    i.putExtra("PATH", path + "/" + str);
                    i.putExtra("SHUFFLE", shuffle);
                    i.putExtra("PATH_ROOT", next_temp);
                    getApplicationContext().startService(i);
                }*/
            }
        });

        final Button pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), mediaplayer.class);
                i.putExtra("PAUSE", "PAUSE");
                getApplicationContext().startService(i);
            }
        });
        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), mediaplayer.class);
                i.putExtra("NEXT", "NEXT");
                i.putExtra("SHUFFLE", shuffle);
                i.putExtra("SONG_LIST", songs);
                getApplicationContext().startService(i);
            }
        });
        /*final Button prev = (Button) findViewById(R.id.next);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), mediaplayer.class);
                i.putExtra("PREV", "PREV");
                i.putExtra("SHUFFLE", shuffle);
                getApplicationContext().startService(i);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //changes the text of the menu item when button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.shuffle:
                if(shuffle) {
                    item.setTitle("Shuffle::OFF");
                    shuffle = false;
                }
                else{
                    item.setTitle("Shuffle::ON");
                    shuffle = true;
                }
                Intent i = new Intent(getApplicationContext(), mediaplayer.class);
                i.putExtra("SHUFFLE", shuffle);
                i.putExtra("UPDATE", "UPDATE");
                i.putExtra("SONG_LIST", songs);
                getApplicationContext().startService(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //sets up the menu item text when starting a new activity
   @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.shuffle);
        if(shuffle){
            item.setTitle("Shuffle::ON");
        }
        else{
            item.setTitle("Shuffle::OFF");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //passes the shuffle variable to the previous activity when the
    //back button is pressed
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("SHUFFLE", shuffle);
        setResult(RESULT_OK, intent);
        finish();
    }

    //accepts the shuffle variable when returning to previous
    //activity when back button is pressed
    //also sets the menu item title depending on
    //the value of the shuffle variable
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Bundle b = data.getExtras();
        if(b != null){
            Boolean shuffleCheck = b.getBoolean("SHUFFLE");
            Boolean test = null;
            if(shuffleCheck != test){
                shuffle = shuffleCheck;
            }
        }
        MenuItem item = menu.findItem(R.id.shuffle);
        if(shuffle){
            item.setTitle("Shuffle::ON");
        }
        else{
            item.setTitle("Shuffle::OFF");
        }
    }

}
