package com.app.ultraplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list);
        runtimePermission();

    }
     public void runtimePermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public ArrayList<File> findSong(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files=file.listFiles();
        for (File songFile: files) {
            if(songFile.isDirectory() && !songFile.isHidden())
                arrayList.addAll(findSong(songFile));
            else
            {
                if(songFile.getName().endsWith(".mp3")|| songFile.getName().endsWith(".wav"))
                {
                    arrayList.add(songFile);
                }
            }
        }

        return arrayList;
    }

    public void display()
    {
        final ArrayList<File> mysongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];
        for(int i=0;i<items.length;i++)
        {
            items[i]=mysongs.get(i).getName().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName=listView.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("Songs",mysongs).putExtra("name",songName).putExtra("pos",position));

            }
        });

    }
}
