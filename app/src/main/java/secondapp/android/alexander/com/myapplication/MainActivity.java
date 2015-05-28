package secondapp.android.alexander.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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
import java.util.List;


public class MainActivity extends Activity {

    private boolean shuffle = false;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle b = getIntent().getExtras();
        String temp  = "/storage/extSdCard/Music";
        final String next_temp = temp;
        if(b != null){
            String tempCheck = b.getString("PATH");
            if(tempCheck != null){
                temp = tempCheck;
            }
            Boolean shuffleCheck = b.getBoolean("SHUFFLE");
            Boolean test = null;
            if(shuffleCheck != test){
                shuffle = shuffleCheck;
            }

        }

        final String path = temp;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.fileList);
        functions f = new functions();
        final List<String> file_list = f.updateFileList(path);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                file_list );
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                String str = (String) adapter.getItemAtPosition(position);
                if (new File(path + "/" + str).isDirectory()) {
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
                }
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
                getApplicationContext().startService(i);
            }
        });
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
