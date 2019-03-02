package com.app.ultraplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Button pause1,next1,previous1;
    SeekBar seekBar;
    TextView name;
    String string;
    MediaPlayer mediaPlayer;
    int positioin;
    Thread updateSeekBar;
    ArrayList<File> mySongs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        pause1 = findViewById(R.id.pauseID);
        next1 = findViewById(R.id.nextID);
        previous1 =findViewById(R.id.previousID);
        seekBar = findViewById(R.id.seekbarID);
        name = findViewById(R.id.songID);

        updateSeekBar = new Thread(){
            @Override
            public void run()
            {
                int tDuration = mediaPlayer.getDuration();
                int current1=0;
                while (current1<tDuration)
                {
                    try {
                        sleep(500);
                        current1=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(current1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        if (mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mySongs=(ArrayList) bundle.getParcelableArrayList("Songs");
        string = mySongs.get(positioin).getName().toString();
        final String sname=intent.getStringExtra("name");

        name.setText(sname);
        name.setSelected(true);
        positioin=bundle.getInt("pos",0);
        Uri uri= (Uri) Uri.parse(String.valueOf(mySongs.get(positioin)));

        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateSeekBar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying())
                {
                    pause1.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    mediaPlayer.pause();

                }
                else
                {
                    pause1.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                positioin=(positioin+1)%mySongs.size();
                Uri u=Uri.parse(mySongs.get(positioin).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                string=mySongs.get(positioin).getName().toString();
                name.setText(string);
                mediaPlayer.start();
            }
        });

        previous1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                positioin=(positioin-1<0)?(mySongs.size()-1):(positioin-1);
                Uri u=Uri.parse(mySongs.get(positioin).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                string=mySongs.get(positioin).getName().toString();
                name.setText(string);
                mediaPlayer.start();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
